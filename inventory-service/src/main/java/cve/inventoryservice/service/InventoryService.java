package cve.inventoryservice.service;

import cve.inventoryservice.model.dto.PagedDto;
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
    PagedDto<InventoryResponseDto> findAll(int pageNo, int pageSize, String sortBy);

    InventoryResponseDto findOne(int id) throws InventoryNotFoundException;

    InventoryResponseDto insert(InventoryCreationDto inventoryCreationDto);

    InventoryResponseDto update(int id, InventoryUpdateDto inventoryUpdateDto) throws InventoryNotFoundException;

    void delete(int id) throws InventoryNotFoundException;

    List<InventoryItemResponseDto> findAllItems(int inventoryId);

    InventoryItemResponseDto findOneItem(int inventoryId, int inventoryItemId);

    InventoryItemResponseDto insertItem(int inventoryId, InventoryItemCreationDto item) throws InventoryNotFoundException;

    InventoryItemResponseDto updateItem(int inventoryId, int inventoryItemId, InventoryItemUpdateDto item) throws InventoryNotFoundException, InventoryItemNotFoundException;

    void deleteItem(int inventoryId, int inventoryItemId) throws InventoryNotFoundException, InventoryItemNotFoundException;
}
