package cve.inventoryservice.controller;

import cve.inventoryservice.model.assembler.InventoryItemItemModelAssembler;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemCreationDto;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemResponseDto;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemUpdateDto;
import cve.inventoryservice.model.dto.inventory.InventoryResponseDto;
import cve.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class InventoryItemControllerTests {
    private InventoryItemController inventoryItemController;

    @Mock
    private InventoryService inventoryService;

    private List<InventoryItemResponseDto> serviceResult;

    @BeforeEach
    public void setUp() {
        inventoryItemController = new InventoryItemController(inventoryService, new InventoryItemItemModelAssembler());

        serviceResult = new ArrayList<>();
        InventoryItemResponseDto inventoryItem1 = new InventoryItemResponseDto();
        inventoryItem1.setId(1);
        inventoryItem1.setVendor("vendor1");
        inventoryItem1.setProduct("product1");
        inventoryItem1.setVersion("version1");

        InventoryItemResponseDto inventoryItem2 = new InventoryItemResponseDto();
        inventoryItem2.setId(2);
        inventoryItem2.setVendor("vendor2");
        inventoryItem2.setProduct("product2");
        inventoryItem2.setVersion("version2");
    }

    @Test
    public void whenAllCalled_thenReturnCollectionOfServiceResult() {
        when(inventoryService.findAllItems(1)).thenReturn(serviceResult);

        CollectionModel<EntityModel<InventoryItemResponseDto>> result = inventoryItemController.all(1);

        assertTrue(result.getContent().stream()
                .allMatch(inventoryItemEntityModel -> serviceResult.stream().anyMatch(i -> i.getId() == inventoryItemEntityModel.getContent().getId())));
    }

    @Test
    public void whenOneCalled_thenReturnModelOfServiceResult() {
        InventoryItemResponseDto inventoryItem = new InventoryItemResponseDto();
        inventoryItem.setId(11);
        inventoryItem.setVendor("vendor1");
        inventoryItem.setProduct("product1");
        inventoryItem.setVersion("version1");

        InventoryResponseDto inventory = new InventoryResponseDto();
        inventory.setId(1);
        inventory.setDomain("domain1");
        inventory.setIp("ip1");
        inventory.setInventoryItems(new HashSet<>());
        inventory.getInventoryItems().add(inventoryItem);

        when(inventoryService.findOneItem(1, 11)).thenReturn(inventoryItem);

        EntityModel<InventoryItemResponseDto> result = inventoryItemController.one(1, 11);

        assertTrue(result.getContent().getId() == inventoryItem.getId());
    }

    @Test
    public void whenCreateCalled_thenReturnModelOfServiceResult() {
        InventoryItemCreationDto inventoryItemCreationDto = new InventoryItemCreationDto();
        inventoryItemCreationDto.setVendor("vendor1");
        inventoryItemCreationDto.setProduct("product1");
        inventoryItemCreationDto.setVersion("version1");

        InventoryItemResponseDto inventoryItemResponseDto = new InventoryItemResponseDto();
        inventoryItemResponseDto.setVendor("vendor1");
        inventoryItemResponseDto.setProduct("product1");
        inventoryItemResponseDto.setVersion("version1");

        when(inventoryService.insertItem(anyInt(), any(InventoryItemCreationDto.class))).thenReturn(inventoryItemResponseDto);

        ResponseEntity<EntityModel<InventoryItemResponseDto>> result = inventoryItemController.createItem(1, inventoryItemCreationDto);

        verify(inventoryService).insertItem(1, inventoryItemCreationDto);
        assertTrue(result.getBody().getContent().getId() == 1);
    }

    @Test
    public void whenUpdateCalled_thenReturnModelOfServiceResult() {
        InventoryItemUpdateDto inventoryItemUpdateDto = new InventoryItemUpdateDto();
        inventoryItemUpdateDto.setVendor("vendor1");
        inventoryItemUpdateDto.setProduct("product1");
        inventoryItemUpdateDto.setVersion("version1");

        InventoryItemResponseDto inventoryItemResponseDto = new InventoryItemResponseDto();
        inventoryItemResponseDto.setVendor("vendor1");
        inventoryItemResponseDto.setProduct("product1");
        inventoryItemResponseDto.setVersion("version1");

        when(inventoryService.updateItem(anyInt(), anyInt(), any(InventoryItemUpdateDto.class))).thenReturn(inventoryItemResponseDto);

        ResponseEntity<EntityModel<InventoryItemResponseDto>> result = inventoryItemController.updateItem(1, 11, inventoryItemUpdateDto);

        verify(inventoryService).updateItem(1, 11, inventoryItemUpdateDto);
        assertTrue(result.getBody().getContent().getId() == 11);
    }

    @Test
    public void whenDeleteCalled_thenReturnModelWithNoContent() {
        ResponseEntity<?> result = inventoryItemController.deleteItem(1, 11);

        verify(inventoryService).deleteItem(1, 11);
        assertTrue(result.getStatusCode() == HttpStatus.NO_CONTENT);
    }

}
