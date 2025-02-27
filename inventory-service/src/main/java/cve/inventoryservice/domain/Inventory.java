package cve.inventoryservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "inventory")
@Access(AccessType.FIELD)
@Getter
@Setter
public class Inventory {
    @Id
    @SequenceGenerator(name = "inventory_id_seq", sequenceName = "inventory_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_id_seq")
    @Column(name = "id", updatable = false)
    private int id;

    private String ip;

    private String domain;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "inventory", orphanRemoval = true)
    private Set<InventoryItem> inventoryItems;


    public Inventory(Set<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public Inventory() {
    }

    public void addInventoryItem(InventoryItem item) {
        item.setInventory(this);
        inventoryItems.add(item);
    }

    public void removeInventoryItem(InventoryItem item) {
        inventoryItems.remove(item);
    }

    public void MapFrom(Inventory inventory) {
        this.ip = inventory.ip;
        this.domain = inventory.domain;
    }
}
