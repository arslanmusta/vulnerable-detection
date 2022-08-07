package cve.inventoryservice.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryItemCreationDto {
    private String vendor;

    private String product;

    private String version;
}
