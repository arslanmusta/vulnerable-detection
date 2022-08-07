package cve.inventoryservice.service;

import cve.inventoryservice.domain.Inventory;
import cve.inventoryservice.domain.InventoryItem;
import cve.inventoryservice.model.dto.inventory.InventoryCreationDto;
import cve.inventoryservice.model.dto.inventory.InventoryResponseDto;
import cve.inventoryservice.model.dto.inventory.InventoryUpdateDto;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemCreationDto;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemResponseDto;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemUpdateDto;
import cve.inventoryservice.model.mapper.InventoryMapper;
import cve.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventoryService {

    private InventoryRepository inventoryRepository;
    private InventoryMapper inventoryMapper;

    public InventoryService(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    public List<InventoryResponseDto> findAll() {
        return inventoryRepository.findAll().stream().map(inventoryMapper::inventoryToInventoryResponseDto).collect(Collectors.toList());
    }

    public InventoryResponseDto findOne(int id) throws InventoryNotFoundException {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));

        return inventoryMapper.inventoryToInventoryResponseDto(inventory);
    }

    public InventoryResponseDto insert(InventoryCreationDto inventoryCreationDto) {
//        if (inventory.getInventoryItems() != null)
//            inventory.getInventoryItems().forEach(i -> i.setInventory(inventory));

        Inventory inventory = inventoryMapper.inventoryCreationDtoToInventory(inventoryCreationDto);

        Inventory persistedInventory = inventoryRepository.save(inventory);

        return inventoryMapper.inventoryToInventoryResponseDto(persistedInventory);
    }

    public InventoryResponseDto update(int id, InventoryUpdateDto inventoryUpdateDto) throws InventoryNotFoundException {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));

        inventory.setIp(inventoryUpdateDto.getIp());
        inventory.setDomain(inventoryUpdateDto.getDomain());


        return inventoryMapper.inventoryToInventoryResponseDto(inventoryRepository.save(inventory));
    }

    public void delete(int id) throws InventoryNotFoundException {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));
        inventoryRepository.delete(inventory);
    }

    public List<InventoryItemResponseDto> findAllItems(int inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));

        return inventory.getInventoryItems().stream().map(inventoryMapper::inventoryItemToInventoryItemResponseDto).collect(Collectors.toList());
    }

    public InventoryItemResponseDto findOneItem(int inventoryId, int inventoryItemId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));

        InventoryItem inventoryItem = inventory.getInventoryItems().stream()
                .filter(i -> i.getId() == inventoryItemId)
                .findFirst()
                .orElseThrow(() -> new InventoryItemNotFoundException(inventoryItemId));

        return inventoryMapper.inventoryItemToInventoryItemResponseDto(inventoryItem);
    }

    public InventoryItemResponseDto insertItem(int id, InventoryItemCreationDto item) throws InventoryNotFoundException {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));

        InventoryItem inventoryItem = inventoryMapper.inventoryItemCreationDtoToInventoryItem(item);

        inventory.addInventoryItem(inventoryItem);

        Inventory updatedInventory = inventoryRepository.save(inventory);
        return inventoryMapper.inventoryItemToInventoryItemResponseDto(updatedInventory.getInventoryItems().stream().max(Comparator.comparingInt(InventoryItem::getId)).orElse(null));
    }

    public InventoryItemResponseDto updateItem(int id, int inventoryItemId, InventoryItemUpdateDto item) throws InventoryNotFoundException, InventoryItemNotFoundException {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));

        InventoryItem inventoryItem = inventory.getInventoryItems().stream()
                .filter(i -> i.getId() == inventoryItemId)
                .findFirst()
                .orElseThrow(() -> new InventoryItemNotFoundException(inventoryItemId));

        inventoryItem.setProduct(item.getProduct());
        inventoryItem.setVendor(item.getVendor());
        inventoryItem.setVersion(item.getVersion());

        Inventory updatedInventory = inventoryRepository.save(inventory);
        return inventoryMapper.inventoryItemToInventoryItemResponseDto(updatedInventory.getInventoryItems().stream().filter(i -> i.getId() == inventoryItemId).findFirst().orElse(null));
    }

    public void deleteItem(int id, int inventoryItemId) throws InventoryNotFoundException, InventoryItemNotFoundException  {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));

        InventoryItem inventoryItem = inventory.getInventoryItems().stream()
                .filter(i -> i.getId() == inventoryItemId)
                .findFirst()
                        .orElseThrow(() -> new InventoryItemNotFoundException(inventoryItemId));

        inventory.removeInventoryItem(inventoryItem);
        inventoryRepository.save(inventory);
    }
}
