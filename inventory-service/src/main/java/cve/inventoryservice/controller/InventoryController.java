package cve.inventoryservice.controller;

import cve.inventoryservice.model.assembler.InventoryModelAssembler;
import cve.inventoryservice.model.dto.PagedDto;
import cve.inventoryservice.model.dto.inventory.InventoryCreationDto;
import cve.inventoryservice.model.dto.inventory.InventoryResponseDto;
import cve.inventoryservice.model.dto.inventory.InventoryUpdateDto;
import cve.inventoryservice.model.validation.StringValues;
import cve.inventoryservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/inventory")
@Validated
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryModelAssembler inventoryModelAssembler;

    public InventoryController(InventoryService inventoryService, InventoryModelAssembler inventoryModelAssembler) {
        this.inventoryService = inventoryService;
        this.inventoryModelAssembler = inventoryModelAssembler;
    }

    @Operation(summary = "Gets all inventories")
    @GetMapping()
    public EntityModel<PagedDto<InventoryResponseDto>> all(
            @Valid @RequestParam(required = false) @NotNull @Min(0)  Integer pageNo,
            @Valid @RequestParam(required = false) @NotNull @Max(1000) Integer pageSize,
            @Valid @RequestParam(required = false) @StringValues(allowedValues = { "id", "domain", "ip" }) String sortBy) {
        return inventoryModelAssembler.toPagedModel(inventoryService.findAll(pageNo, pageSize, sortBy));
    }

    @Operation(summary = "Gets an inventory")
    @GetMapping("/{id}")
    public EntityModel<InventoryResponseDto> one(@PathVariable int id) {
        return inventoryModelAssembler.toModel(inventoryService.findOne(id));
    }

    @Operation(summary = "Creates an inventory")
    @ApiResponse(responseCode = "201", description = "Inventory created")
    @PostMapping()
    ResponseEntity<EntityModel<InventoryResponseDto>> create(@Valid @RequestBody InventoryCreationDto inventoryCreationDto) {
        InventoryResponseDto inventoryResponseDto = inventoryService.insert(inventoryCreationDto);
        EntityModel<InventoryResponseDto> entityModel = inventoryModelAssembler.toModel(inventoryResponseDto);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "Modifies an inventory")
    @ApiResponse(responseCode = "201", description = "Inventory modified")
    @PutMapping("/{id}")
    ResponseEntity<EntityModel<InventoryResponseDto>> update(@PathVariable int id, @RequestBody InventoryUpdateDto inventoryUpdateDto) {
        InventoryResponseDto inventoryResponseDto = inventoryService.update(id, inventoryUpdateDto);

        EntityModel<InventoryResponseDto> entityModel = inventoryModelAssembler.toModel(inventoryResponseDto);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "Deletes an inventory")
    @ApiResponse(responseCode = "204", description = "Inventory deleted")
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable int id) {
        inventoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
