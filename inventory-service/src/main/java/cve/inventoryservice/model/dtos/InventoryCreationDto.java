package cve.inventoryservice.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryCreationDto {
    private String ip;

    private String domain;
}
