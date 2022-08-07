package cve.inventoryservice.model.mappers;

import cve.inventoryservice.domain.Inventory;
import cve.inventoryservice.domain.InventoryItem;
import cve.inventoryservice.model.dtos.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    InventoryResponseDto inventoryToInventoryResponseDto(Inventory inventory);

    InventoryItemResponseDto inventoryItemToInventoryItemResponseDto(InventoryItem inventoryItem);

    Inventory inventoryCreationDtoToInventory(InventoryCreationDto inventoryCreationDto);

    Inventory inventoryUpdateDtoToInventory(InventoryUpdateDto inventoryUpdateDto);

    InventoryItem inventoryItemCreationDtoToInventoryItem(InventoryItemCreationDto inventoryItemCreationDto);

}
