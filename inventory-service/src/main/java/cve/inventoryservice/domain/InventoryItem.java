package cve.inventoryservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "inventory_item")
@Access(AccessType.FIELD)
@Getter
@Setter
public class InventoryItem {
    @Id
    @SequenceGenerator(name = "inventory_item_id_seq", sequenceName = "inventory_item_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_item_id_seq")
    @Column(name = "id", updatable = false)
    private int id;

    private String vendor;

    private String product;

    private String version;

    @ManyToOne(fetch = FetchType.LAZY)
    private Inventory inventory;
}
