package cve.inventoryservice.service.exception;

import lombok.Getter;

@Getter
public class InventoryNotFoundException extends RuntimeException {
    private final int id;

    public InventoryNotFoundException(int id) {
        super("Cannot found inventory: " + id);
        this.id = id;
    }
}
