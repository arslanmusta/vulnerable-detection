package cve.inventoryservice.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryUpdateDto {
    private String ip;

    private String domain;
}
