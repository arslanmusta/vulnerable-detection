package cve.inventoryservice.model.dto.inventory;

import cve.inventoryservice.model.dto.inventoryitem.InventoryItemResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class InventoryResponseDto {
    private int id;

    private String ip;

    private String domain;

    private Set<InventoryItemResponseDto> inventoryItems;
}
