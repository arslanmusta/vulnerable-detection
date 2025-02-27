package cve.inventoryservice.service.exception;

import lombok.Getter;

@Getter
public class InventoryItemNotFoundException extends RuntimeException {
    private final int id;

    public InventoryItemNotFoundException(int id) {
        super("Cannot found inventory item: " + id);
        this.id = id;
    }
}
