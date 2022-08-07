package cve.inventoryservice.service;

import cve.inventoryservice.model.dto.inventory.InventoryCreationDto;
import cve.inventoryservice.model.dto.inventory.InventoryResponseDto;
import cve.inventoryservice.model.dto.inventory.InventoryUpdateDto;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemCreationDto;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemResponseDto;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemUpdateDto;
import cve.inventoryservice.service.exception.InventoryItemNotFoundException;
import cve.inventoryservice.service.exception.InventoryNotFoundException;

import java.util.List;

public interface InventoryService {
    List<InventoryResponseDto> findAll();

    InventoryResponseDto findOne(int id) throws InventoryNotFoundException;

    InventoryResponseDto insert(InventoryCreationDto inventoryCreationDto);

    InventoryResponseDto update(int id, InventoryUpdateDto inventoryUpdateDto) throws InventoryNotFoundException;

    void delete(int id) throws InventoryNotFoundException;

    List<InventoryItemResponseDto> findAllItems(int inventoryId);

    InventoryItemResponseDto findOneItem(int inventoryId, int inventoryItemId);

    InventoryItemResponseDto insertItem(int id, InventoryItemCreationDto item) throws InventoryNotFoundException;

    InventoryItemResponseDto updateItem(int id, int inventoryItemId, InventoryItemUpdateDto item) throws InventoryNotFoundException, InventoryItemNotFoundException;

    void deleteItem(int id, int inventoryItemId) throws InventoryNotFoundException, InventoryItemNotFoundException;
}
