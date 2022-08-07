package cve.inventoryservice.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryItemResponseDto {
    private int id;

    private int inventoryId;

    private String vendor;

    private String product;

    private String version;
}
