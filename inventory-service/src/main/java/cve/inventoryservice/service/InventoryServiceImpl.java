package cve.inventoryservice.service;

import cve.inventoryservice.domain.Inventory;
import cve.inventoryservice.domain.InventoryItem;
import cve.inventoryservice.model.dto.PagedDto;
import cve.inventoryservice.model.dto.inventory.InventoryCreationDto;
import cve.inventoryservice.model.dto.inventory.InventoryResponseDto;
import cve.inventoryservice.model.dto.inventory.InventoryUpdateDto;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemCreationDto;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemResponseDto;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemUpdateDto;
import cve.inventoryservice.model.mapper.InventoryMapper;
import cve.inventoryservice.repository.InventoryRepository;
import cve.inventoryservice.service.exception.InventoryItemNotFoundException;
import cve.inventoryservice.service.exception.InventoryNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public PagedDto<InventoryResponseDto> findAll(int pageNo, int pageSize, String sortBy) {
        Pageable pageable =
                sortBy == null
                        ? PageRequest.of(pageNo, pageSize)
                        : PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Inventory> pagedResult = inventoryRepository.findAll(pageable);

        PagedDto<InventoryResponseDto> inventoryResponsePagedDto = new PagedDto<>();

        inventoryResponsePagedDto.setPageNo(pagedResult.getNumber());
        inventoryResponsePagedDto.setPageSize(pagedResult.getSize());
        inventoryResponsePagedDto.setSort(pagedResult.getSort().toString());
        inventoryResponsePagedDto.setTotalPage(pagedResult.getTotalPages());

        inventoryResponsePagedDto.setItems(
                pagedResult.hasContent()
                        ? pagedResult.getContent().stream().map(inventoryMapper::inventoryToInventoryResponseDto).collect(Collectors.toList())
                        : new ArrayList<>());

        return inventoryResponsePagedDto;
    }

    @Override
    public InventoryResponseDto findOne(int id) throws InventoryNotFoundException {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));

        return inventoryMapper.inventoryToInventoryResponseDto(inventory);
    }

    @Override
    public InventoryResponseDto insert(InventoryCreationDto inventoryCreationDto) {
        Inventory inventory = inventoryMapper.inventoryCreationDtoToInventory(inventoryCreationDto);

        Inventory persistedInventory = inventoryRepository.save(inventory);

        return inventoryMapper.inventoryToInventoryResponseDto(persistedInventory);
    }

    @Override
    public InventoryResponseDto update(int id, InventoryUpdateDto inventoryUpdateDto) throws InventoryNotFoundException {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));

        inventoryMapper.updateInventory(inventory, inventoryUpdateDto);

        return inventoryMapper.inventoryToInventoryResponseDto(inventoryRepository.save(inventory));
    }

    @Override
    public void delete(int id) throws InventoryNotFoundException {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));
        inventoryRepository.delete(inventory);
    }

    @Override
    public List<InventoryItemResponseDto> findAllItems(int inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));

        return inventory.getInventoryItems().stream().map(inventoryMapper::inventoryItemToInventoryItemResponseDto).collect(Collectors.toList());
    }

    @Override
    public InventoryItemResponseDto findOneItem(int inventoryId, int inventoryItemId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));

        InventoryItem inventoryItem = inventory.getInventoryItems().stream()
                .filter(i -> i.getId() == inventoryItemId)
                .findFirst()
                .orElseThrow(() -> new InventoryItemNotFoundException(inventoryItemId));

        return inventoryMapper.inventoryItemToInventoryItemResponseDto(inventoryItem);
    }

    @Override
    public InventoryItemResponseDto insertItem(int inventoryId, InventoryItemCreationDto item) throws InventoryNotFoundException {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));

        InventoryItem inventoryItem = inventoryMapper.inventoryItemCreationDtoToInventoryItem(item);

        inventory.addInventoryItem(inventoryItem);

        Inventory updatedInventory = inventoryRepository.save(inventory);
        return inventoryMapper.inventoryItemToInventoryItemResponseDto(updatedInventory.getInventoryItems().stream().max(Comparator.comparingInt(InventoryItem::getId)).orElse(null));
    }

    @Override
    public InventoryItemResponseDto updateItem(int inventoryId, int inventoryItemId, InventoryItemUpdateDto item) throws InventoryNotFoundException, InventoryItemNotFoundException {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));

        InventoryItem inventoryItem = inventory.getInventoryItems().stream()
                .filter(i -> i.getId() == inventoryItemId)
                .findFirst()
                .orElseThrow(() -> new InventoryItemNotFoundException(inventoryItemId));

        inventoryMapper.updateInventoryItem(inventoryItem, item);

        Inventory updatedInventory = inventoryRepository.save(inventory);
        return inventoryMapper.inventoryItemToInventoryItemResponseDto(updatedInventory.getInventoryItems().stream().filter(i -> i.getId() == inventoryItemId).findFirst().orElse(null));
    }

    @Override
    public void deleteItem(int inventoryId, int inventoryItemId) throws InventoryNotFoundException, InventoryItemNotFoundException  {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));

        InventoryItem inventoryItem = inventory.getInventoryItems().stream()
                .filter(i -> i.getId() == inventoryItemId)
                .findFirst()
                        .orElseThrow(() -> new InventoryItemNotFoundException(inventoryItemId));

        inventory.removeInventoryItem(inventoryItem);
        inventoryRepository.save(inventory);
    }
}
