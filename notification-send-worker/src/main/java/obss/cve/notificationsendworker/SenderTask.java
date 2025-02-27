package obss.cve.notificationsendworker;

import obss.cve.notificationsendworker.domain.Config;
import obss.cve.notificationsendworker.domain.Notification;
import obss.cve.notificationsendworker.model.Inventory;
import obss.cve.notificationsendworker.model.InventoryItem;
import obss.cve.notificationsendworker.proxy.InventoryServiceProxy;
import obss.cve.notificationsendworker.proxy.JiraProxy;
import obss.cve.notificationsendworker.proxy.MailProxy;
import obss.cve.notificationsendworker.repository.ConfigRepository;
import obss.cve.notificationsendworker.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class SenderTask {

    private final NotificationRepository notificationRepository;
    private final ConfigRepository configRepository;
    private final InventoryServiceProxy inventoryServiceProxy;
    private final MailProxy mailProxy;
    private final JiraProxy jiraProxy;

    private static final Logger log = LoggerFactory.getLogger(SenderTask.class);

    public SenderTask(NotificationRepository notificationRepository, ConfigRepository configRepository, InventoryServiceProxy inventoryServiceProxy, MailProxy mailProxy, JiraProxy jiraProxy) {
        this.notificationRepository = notificationRepository;
        this.configRepository = configRepository;
        this.inventoryServiceProxy = inventoryServiceProxy;
        this.mailProxy = mailProxy;
        this.jiraProxy = jiraProxy;
    }

    @Scheduled(fixedDelayString = "${notification.send.period}", initialDelayString = "${notification.send.delay}")
    public void checkNotification() {
        List<Notification> notificationList = notificationRepository.GetAllUnsent();

        notificationList.forEach(notification -> {
            try {
                Inventory inventory = inventoryServiceProxy.GetInventory(notification.getInventoryId());
                InventoryItem inventoryItem = inventory.getInventoryItems().stream()
                        .filter(item -> item.getId() == notification.getInventoryItemId())
                        .findFirst()
                            .orElseThrow();

                String subject = String.format("Güvenlik Açığı: %s", inventory.getDomain());

                String text = String.format("%s cihazındaki %s %s %s yazılım/donanımda %s kodlu güvenlik açığı tespit edilmiştir",
                        inventory.getDomain(),
                        inventoryItem.getVendor(),
                        inventoryItem.getProduct(),
                        inventoryItem.getVersion(),
                        notification.getCveId());

                if (!notification.isMailSend()) {

                    String[] emails = configRepository.findById("email").orElse(new Config("email", "")).getValue().split(",", 0);

                    if (!emails[0].equals("")) {
                        boolean result = false;
                        for (int i = 0; i < emails.length; i++) {
                            result |= mailProxy.sendMail(emails[i], subject, text);
                        }

                        notification.setMailSend(result);
                        notificationRepository.save(notification);
                    }
                }

                if (!notification.isJiraIssueCreated()) {

                    boolean result = jiraProxy.sendIssue(subject, text);
                    notification.setJiraIssueCreated(result);
                    notificationRepository.save(notification);
                }
            }
            catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        });
    }
}
