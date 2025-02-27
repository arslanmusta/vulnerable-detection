package cve.inventoryservice.model.assembler;

import cve.inventoryservice.controller.InventoryController;
import cve.inventoryservice.model.dto.PagedDto;
import cve.inventoryservice.model.dto.inventory.InventoryResponseDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@SuppressWarnings("NullableProblems")
@Component
public class InventoryModelAssembler implements RepresentationModelAssembler<InventoryResponseDto, EntityModel<InventoryResponseDto>> {
    @Override
    public EntityModel<InventoryResponseDto> toModel(InventoryResponseDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(InventoryController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(InventoryController.class).all(0, 1000, "id")).withRel("inventories"));
    }

    @Override
    public CollectionModel<EntityModel<InventoryResponseDto>> toCollectionModel(Iterable<? extends InventoryResponseDto> entities) {
        return RepresentationModelAssembler.super
                .toCollectionModel(entities)
                .add(linkTo(methodOn(InventoryController.class).all(0, 1000, "id")).withSelfRel());
    }

    public EntityModel<PagedDto<InventoryResponseDto>> toPagedModel(PagedDto<InventoryResponseDto> entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(InventoryController.class).all(0, 1000, "id")).withRel("inventories"));
    }
}
