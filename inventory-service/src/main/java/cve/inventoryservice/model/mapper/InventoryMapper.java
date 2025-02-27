package cve.inventoryservice.model.mapper;

import cve.inventoryservice.domain.Inventory;
import cve.inventoryservice.domain.InventoryItem;
import cve.inventoryservice.model.dto.inventory.InventoryCreationDto;
import cve.inventoryservice.model.dto.inventory.InventoryResponseDto;
import cve.inventoryservice.model.dto.inventory.InventoryUpdateDto;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemCreationDto;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemResponseDto;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    InventoryResponseDto inventoryToInventoryResponseDto(Inventory inventory);

    @Mapping(target = "inventoryId", ignore = true)
    InventoryItemResponseDto inventoryItemToInventoryItemResponseDto(InventoryItem inventoryItem);

    @Mapping(target = "inventoryItems", ignore = true)
    @Mapping(target = "id", ignore = true)
    Inventory inventoryCreationDtoToInventory(InventoryCreationDto inventoryCreationDto);

    @Mapping(target = "inventoryItems", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateInventory(@MappingTarget Inventory inventory, InventoryUpdateDto inventoryUpdateDto);

    @Mapping(target = "inventory", ignore = true)
    @Mapping(target = "id", ignore = true)
    InventoryItem inventoryItemCreationDtoToInventoryItem(InventoryItemCreationDto inventoryItemCreationDto);

    @Mapping(target = "inventory", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateInventoryItem(@MappingTarget InventoryItem inventoryItem, InventoryItemUpdateDto inventoryItemUpdateDto);

}
