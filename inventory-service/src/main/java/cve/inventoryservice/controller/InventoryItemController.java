package cve.inventoryservice.controller;

import cve.inventoryservice.domain.InventoryItem;
import cve.inventoryservice.model.InventoryItemItemModelAssembler;
import cve.inventoryservice.model.dtos.InventoryItemCreationDto;
import cve.inventoryservice.model.dtos.InventoryItemResponseDto;
import cve.inventoryservice.model.dtos.InventoryItemUpdateDto;
import cve.inventoryservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class InventoryItemController {

    private final InventoryService inventoryService;
    private final InventoryItemItemModelAssembler inventoryItemItemModelAssembler;

    public InventoryItemController(InventoryService inventoryService, InventoryItemItemModelAssembler inventoryItemItemModelAssembler) {
        this.inventoryService = inventoryService;
        this.inventoryItemItemModelAssembler = inventoryItemItemModelAssembler;
    }

    @Operation(summary = "Gets all inventory items of an inventory")
    @GetMapping("/inventory/{id}/item")
    public CollectionModel<EntityModel<InventoryItemResponseDto>> all(@PathVariable int id) {
        List<InventoryItemResponseDto> inventoryItemSet = inventoryService.findAllItems(id);

        return inventoryItemItemModelAssembler.toCollectionModel(inventoryItemSet);
    }

    @Operation(summary = "Gets an inventory item of an inventory")
    @GetMapping("/inventory/{inventoryId}/item/{inventoryItemId}")
    public EntityModel<InventoryItemResponseDto> one(@PathVariable int inventoryId, @PathVariable int inventoryItemId) {
        InventoryItemResponseDto inventoryItemResponseDto = inventoryService.findOneItem(inventoryId, inventoryItemId);

        return inventoryItemItemModelAssembler.toModel(inventoryItemResponseDto);
    }

    @Operation(summary = "Creates an inventory item")
    @ApiResponse(responseCode = "201", description = "Inventory item created")
    @PostMapping("/inventory/{id}/item")
    ResponseEntity<EntityModel<InventoryItemResponseDto>> createItem(@PathVariable int id, @RequestBody InventoryItemCreationDto item) {
        InventoryItemResponseDto newItem = inventoryService.insertItem(id, item);

        EntityModel<InventoryItemResponseDto> entityModel = inventoryItemItemModelAssembler.toModel(newItem);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "Modifies an inventory item")
    @ApiResponse(responseCode = "201", description = "Inventory item modified")
    @PutMapping("/inventory/{inventoryId}/item/{inventoryItemId}")
    ResponseEntity<EntityModel<InventoryItemResponseDto>> updateItem(@PathVariable int inventoryId, @PathVariable int inventoryItemId, @RequestBody InventoryItemUpdateDto item) {
        InventoryItemResponseDto updatedInventoryItem = inventoryService.updateItem(inventoryId, inventoryItemId, item);

        EntityModel<InventoryItemResponseDto> entityModel = inventoryItemItemModelAssembler.toModel(updatedInventoryItem);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "Deletes an inventory item")
    @ApiResponse(responseCode = "204", description = "Inventory item deleted")
    @DeleteMapping("/inventory/{inventoryId}/item/{inventoryItemId}")
    ResponseEntity<?> deleteItem(@PathVariable int inventoryId, @PathVariable int inventoryItemId) {
        inventoryService.deleteItem(inventoryId, inventoryItemId);

        return ResponseEntity.noContent().build();
    }
}
