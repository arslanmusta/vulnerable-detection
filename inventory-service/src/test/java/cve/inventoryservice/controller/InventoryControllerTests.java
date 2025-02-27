package cve.inventoryservice.controller;

import cve.inventoryservice.domain.Inventory;
import cve.inventoryservice.model.assembler.InventoryModelAssembler;
import cve.inventoryservice.model.dto.PagedDto;
import cve.inventoryservice.model.dto.inventory.InventoryCreationDto;
import cve.inventoryservice.model.dto.inventory.InventoryResponseDto;
import cve.inventoryservice.model.dto.inventory.InventoryUpdateDto;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class InventoryControllerTests {

    private InventoryController inventoryController;

    @Mock
    private InventoryService inventoryService;

    private PagedDto<InventoryResponseDto> serviceResult;

    @BeforeEach
    public void setUp() {
        inventoryController = new InventoryController(inventoryService, new InventoryModelAssembler());

        InventoryResponseDto inventory1 = new InventoryResponseDto();
        inventory1.setId(1);
        inventory1.setDomain("domain1");
        inventory1.setIp("ip1");

        InventoryResponseDto inventory2 = new InventoryResponseDto();
        inventory1.setId(2);
        inventory1.setDomain("domain2");
        inventory1.setIp("ip2");

        serviceResult.setItems(new ArrayList<>());

        serviceResult.getItems().add(inventory1);
        serviceResult.getItems().add(inventory2);
    }

    @Test
    public void whenAllCalled_thenReturnCollectionOfServiceResult() {
        when(inventoryService.findAll(10, 10, "")).thenReturn(serviceResult);

        EntityModel<PagedDto<InventoryResponseDto>> result = inventoryController.all(10, 10, "");

        assertTrue(result.getContent().getItems().stream().allMatch(inventoryEntityModel -> serviceResult.getItems().stream().anyMatch(i -> i.getId() == inventoryEntityModel.getId())));
    }

    @Test
    public void whenOneCalled_thenReturnModelOfServiceResult() {
        Inventory inventory = new Inventory();
        inventory.setId(1);
        inventory.setDomain("domain1");
        inventory.setIp("ip1");

        InventoryResponseDto inventoryResponseDto = new InventoryResponseDto();

        when(inventoryService.findOne(1)).thenReturn(inventoryResponseDto);

        EntityModel<InventoryResponseDto> result = inventoryController.one(1);

        assertTrue(result.getContent().getId() == inventory.getId());
    }

    @Test
    public void whenCreateCalled_thenReturnModelOfServiceResult() {
        Inventory inventory = new Inventory();
        inventory.setId(1);
        inventory.setDomain("domain1");
        inventory.setIp("ip1");

        InventoryCreationDto inventoryCreationDto = new InventoryCreationDto();
        InventoryResponseDto inventoryResponseDto = new InventoryResponseDto();

        when(inventoryService.insert(any(InventoryCreationDto.class))).thenReturn(inventoryResponseDto);

        ResponseEntity<EntityModel<InventoryResponseDto>> result = inventoryController.create(inventoryCreationDto);

        verify(inventoryService).insert(inventoryCreationDto);
        assertTrue(result.getBody().getContent().getId() == inventory.getId());
    }

    @Test
    public void whenUpdateCalled_thenReturnModelOfServiceResult() {
        Inventory inventory = new Inventory();
        inventory.setId(1);
        inventory.setDomain("domain1");
        inventory.setIp("ip1");

        when(inventoryService.update(anyInt(), any(InventoryUpdateDto.class))).thenReturn(new InventoryResponseDto());

        ResponseEntity<EntityModel<InventoryResponseDto>> result = inventoryController.update(1, new InventoryUpdateDto());

        verify(inventoryService).update(1, new InventoryUpdateDto());
        assertTrue(result.getBody().getContent().getId() == inventory.getId());
    }

    @Test
    public void whenDeleteCalled_thenReturnModelWithNoContent() {
        ResponseEntity<?> result = inventoryController.delete(1);

        verify(inventoryService).delete(1);
        assertTrue(result.getStatusCode() == HttpStatus.NO_CONTENT);
    }

}
