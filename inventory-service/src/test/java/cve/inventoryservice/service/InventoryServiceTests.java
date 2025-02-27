package cve.inventoryservice.service;

import cve.inventoryservice.domain.Inventory;
import cve.inventoryservice.domain.InventoryItem;
import cve.inventoryservice.model.dto.PagedDto;
import cve.inventoryservice.model.dto.inventory.InventoryCreationDto;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemResponseDto;
import cve.inventoryservice.model.dto.inventory.InventoryResponseDto;
import cve.inventoryservice.model.dto.inventory.InventoryUpdateDto;
import cve.inventoryservice.model.mapper.InventoryMapper;
import cve.inventoryservice.repository.InventoryRepository;
import cve.inventoryservice.service.exception.InventoryItemNotFoundException;
import cve.inventoryservice.service.exception.InventoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InventoryServiceTests {

    private InventoryService inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    private List<Inventory> repoResult;

    private InventoryMapper inventoryMapper = Mappers.getMapper(InventoryMapper.class);

    @BeforeEach
    public void setUp() {
        repoResult = new ArrayList<>();

        Inventory inventory1 = new Inventory();
        inventory1.setId(1);
        inventory1.setDomain("domain1");
        inventory1.setIp("ip1");

        Inventory inventory2 = new Inventory();
        inventory1.setId(2);
        inventory1.setDomain("domain2");
        inventory1.setIp("ip2");

        repoResult.add(inventory1);
        repoResult.add(inventory2);

        inventoryService = new InventoryServiceImpl(inventoryRepository, inventoryMapper);
    }

    @Test
    public void whenFindAllCalled_thenReturnRepositoryResult() {
        when(inventoryRepository.findAll()).thenReturn(repoResult);

        PagedDto<InventoryResponseDto> result = inventoryService.findAll(10, 10, "");

        verify(inventoryRepository, times(1)).findAll();
        assertSame(result, repoResult);
    }

    @Test
    public void whenFindOneCalled_thenReturnResult() {
        Inventory persistedInventory = new Inventory();
        persistedInventory.setId(1);
        persistedInventory.setDomain("domain1");
        persistedInventory.setIp("ip1");

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(persistedInventory));

        InventoryResponseDto result = inventoryService.findOne(1);

        assertSame(result, persistedInventory);
    }

    @Test
    public void whenFindOneCalledWithNonExistsInventory_thenThrowInventoryNotFoundException() {
        when(inventoryRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class, () -> inventoryService.findOne(1));
    }

    @Test
    public void whenInsertCalled_thenCallRepositorySave() {
        Inventory newInventory = new Inventory(new HashSet<>());
        inventoryService.insert(new InventoryCreationDto());

        verify(inventoryRepository).save(newInventory);
    }

    @Test
    public void whenUpdateCalled_thenCallRepositorySave() {
        Inventory persistedInventory = new Inventory();
        persistedInventory.setId(1);
        persistedInventory.setDomain("domain1");
        persistedInventory.setIp("ip1");

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(persistedInventory));

        Inventory inventory = new Inventory();
        inventory.setId(1);
        inventory.setDomain("newDomain");
        inventory.setIp("newIp");

        inventoryService.update(inventory.getId(), new InventoryUpdateDto());

        verify(inventoryRepository).save(persistedInventory);
        assertEquals(inventory.getDomain(), persistedInventory.getDomain());
        assertEquals(inventory.getIp(), persistedInventory.getIp());
    }

    @Test
    public void whenUpdateCalledWithNonExistsInventory_thenThrowInventoryNotFoundException() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());

        Inventory inventory = new Inventory();
        inventory.setId(1);
        inventory.setDomain("newDomain");
        inventory.setIp("newIp");

        assertThrows(InventoryNotFoundException.class, () -> inventoryService.update(inventory.getId(), new InventoryUpdateDto()));
    }

    @Test
    public void whenDeleteCalled_thenCallRepositoryDelete() {
        Inventory persistedInventory = new Inventory();
        persistedInventory.setId(1);
        persistedInventory.setDomain("domain1");
        persistedInventory.setIp("ip1");

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(persistedInventory));

        inventoryService.delete(1);

        verify(inventoryRepository).delete(persistedInventory);
    }

    @Test
    public void whenDeleteCalledWithNonExistsInventory_thenThrowInventoryNotFoundException() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class, () -> inventoryService.delete(1));
    }

    @Test
    public void whenFindAllItemsCalled_thenReturnRepositoryResult() {
        Inventory persistedInventory = new Inventory(new HashSet<>());
        persistedInventory.setId(1);
        persistedInventory.setDomain("domain1");
        persistedInventory.setIp("ip1");

        InventoryItem persistedInventoryItem = new InventoryItem();

        persistedInventory.addInventoryItem(persistedInventoryItem);

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(persistedInventory));

        List<InventoryItemResponseDto> result = inventoryService.findAllItems(1);

        assertTrue(result.contains(persistedInventoryItem));
    }

    @Test
    public void whenFindAllItemsCalledWithNonExistsInventory_thenThrowInventoryNotFoundException() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class, () -> inventoryService.findAllItems(1));
    }

    @Test
    public void whenFindOneItemCalled_thenReturnRepositoryResult() {
        Inventory persistedInventory = new Inventory(new HashSet<>());
        persistedInventory.setId(1);
        persistedInventory.setDomain("domain1");
        persistedInventory.setIp("ip1");

        InventoryItem persistedInventoryItem = new InventoryItem();
        persistedInventoryItem.setId(11);

        persistedInventory.addInventoryItem(persistedInventoryItem);

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(persistedInventory));

        InventoryItemResponseDto result = inventoryService.findOneItem(1, 11);

        assertSame(result, persistedInventoryItem);
    }

    @Test
    public void whenFindOneItemCalledWithNonExistsInventory_thenThrowInventoryNotFoundException() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class, () -> inventoryService.findOneItem(1, 11));
    }

    @Test
    public void whenFindOneItemCalledWithNonExistsInventoryItem_thenThrowInventoryItemNotFoundException() {
        Inventory persistedInventory = new Inventory(new HashSet<>());
        persistedInventory.setId(1);
        persistedInventory.setDomain("domain1");
        persistedInventory.setIp("ip1");

        InventoryItem persistedInventoryItem = new InventoryItem();
        persistedInventoryItem.setId(11);

        persistedInventory.addInventoryItem(persistedInventoryItem);

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(persistedInventory));

        assertThrows(InventoryItemNotFoundException.class, () -> inventoryService.findOneItem(1, 22));
    }

    @Test
    public void whenInsertItemCalled_thenCallRepositorySave() {
        Inventory persistedInventory = new Inventory(new HashSet<>());
        persistedInventory.setId(1);
        persistedInventory.setDomain("domain1");
        persistedInventory.setIp("ip1");

        InventoryItem persistedInventoryItem = new InventoryItem();
        persistedInventoryItem.setId(11);

        persistedInventory.addInventoryItem(persistedInventoryItem);

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(persistedInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(new Inventory(new HashSet<>()));

        inventoryService.insertItem(1, null);

        verify(inventoryRepository).save(persistedInventory);
    }

    @Test
    public void whenInsertItemCalledWithNonExistsInventory_thenThrowInventoryNotFoundException() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class, () -> inventoryService.insertItem(1, null));
    }

    @Test
    public void whenUpdateItemCalled_thenCallRepositorySave() {
        Inventory persistedInventory = new Inventory(new HashSet<>());
        persistedInventory.setId(1);
        persistedInventory.setDomain("domain1");
        persistedInventory.setIp("ip1");

        InventoryItem persistedInventoryItem = new InventoryItem();
        persistedInventoryItem.setId(11);

        persistedInventory.addInventoryItem(persistedInventoryItem);

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(persistedInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(new Inventory(new HashSet<>()));

        inventoryService.updateItem(1, 11, null);

        verify(inventoryRepository).save(persistedInventory);
    }

    @Test
    public void whenUpdateItemCalledWithNonExistsInventory_thenThrowInventoryNotFoundException() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class, () -> inventoryService.updateItem(1, 11, null));
    }

    @Test
    public void whenUpdateItemCalledWithNonExistsInventoryItem_thenThrowInventoryItemNotFoundException() {
        Inventory persistedInventory = new Inventory(new HashSet<>());
        persistedInventory.setId(1);
        persistedInventory.setDomain("domain1");
        persistedInventory.setIp("ip1");

        InventoryItem persistedInventoryItem = new InventoryItem();
        persistedInventoryItem.setId(11);

        persistedInventory.addInventoryItem(persistedInventoryItem);

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(persistedInventory));

        assertThrows(InventoryItemNotFoundException.class, () -> inventoryService.updateItem(1, 22, null));
    }

    @Test
    public void whenDeleteItemCalled_thenCallRepositorySave() {
        Inventory persistedInventory = new Inventory(new HashSet<>());
        persistedInventory.setId(1);
        persistedInventory.setDomain("domain1");
        persistedInventory.setIp("ip1");

        InventoryItem persistedInventoryItem = new InventoryItem();
        persistedInventoryItem.setId(11);

        persistedInventory.addInventoryItem(persistedInventoryItem);

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(persistedInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(new Inventory(new HashSet<>()));

        inventoryService.deleteItem(1, 11);

        verify(inventoryRepository).save(persistedInventory);
    }

    @Test
    public void whenDeleteItemCalledWithNonExistsInventory_thenThrowInventoryNotFoundException() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class, () -> inventoryService.deleteItem(1, 11));
    }

    @Test
    public void whenDeleteItemCalledWithNonExistsInventoryItem_thenThrowInventoryItemNotFoundException() {
        Inventory persistedInventory = new Inventory(new HashSet<>());
        persistedInventory.setId(1);
        persistedInventory.setDomain("domain1");
        persistedInventory.setIp("ip1");

        InventoryItem persistedInventoryItem = new InventoryItem();
        persistedInventoryItem.setId(11);

        persistedInventory.addInventoryItem(persistedInventoryItem);

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(persistedInventory));

        assertThrows(InventoryItemNotFoundException.class, () -> inventoryService.deleteItem(1, 22));
    }

}
