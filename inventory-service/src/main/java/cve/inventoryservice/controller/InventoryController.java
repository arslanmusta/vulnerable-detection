package cve.inventoryservice.controller;

import cve.inventoryservice.domain.Inventory;
import cve.inventoryservice.model.InventoryModelAssembler;
import cve.inventoryservice.model.dtos.InventoryCreationDto;
import cve.inventoryservice.model.dtos.InventoryResponseDto;
import cve.inventoryservice.model.dtos.InventoryUpdateDto;
import cve.inventoryservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryModelAssembler inventoryModelAssembler;

    public InventoryController(InventoryService inventoryService, InventoryModelAssembler inventoryModelAssembler) {
        this.inventoryService = inventoryService;
        this.inventoryModelAssembler = inventoryModelAssembler;
    }

    @Operation(summary = "Gets all inventories")
    @GetMapping("/inventory")
    public CollectionModel<EntityModel<InventoryResponseDto>> all() {
        return inventoryModelAssembler.toCollectionModel(inventoryService.findAll());
    }

    @Operation(summary = "Gets an inventory")
    @GetMapping("/inventory/{id}")
    public EntityModel<InventoryResponseDto> one(@PathVariable int id) {
        return inventoryModelAssembler.toModel(inventoryService.findOne(id));
    }

    @Operation(summary = "Creates an inventory")
    @ApiResponse(responseCode = "201", description = "Inventory created")
    @PostMapping("/inventory")
    ResponseEntity<EntityModel<InventoryResponseDto>> create(@RequestBody InventoryCreationDto inventoryCreationDto) {
        InventoryResponseDto inventoryResponseDto = inventoryService.insert(inventoryCreationDto);
        EntityModel<InventoryResponseDto> entityModel = inventoryModelAssembler.toModel(inventoryResponseDto);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "Modifies an inventory")
    @ApiResponse(responseCode = "201", description = "Inventory modified")
    @PutMapping("/inventory/{id}")
    ResponseEntity<EntityModel<InventoryResponseDto>> update(@PathVariable int id, @RequestBody InventoryUpdateDto inventoryUpdateDto) {
        InventoryResponseDto inventoryResponseDto = inventoryService.update(id, inventoryUpdateDto);

        EntityModel<InventoryResponseDto> entityModel = inventoryModelAssembler.toModel(inventoryResponseDto);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "Deletes an inventory")
    @ApiResponse(responseCode = "204", description = "Inventory deleted")
    @DeleteMapping("/inventory/{id}")
    ResponseEntity<?> delete(@PathVariable int id) {
        inventoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
