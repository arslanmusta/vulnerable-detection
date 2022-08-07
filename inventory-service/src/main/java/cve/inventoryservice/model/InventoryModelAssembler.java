package cve.inventoryservice.model;

import cve.inventoryservice.controller.InventoryController;
import cve.inventoryservice.model.dtos.InventoryResponseDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class InventoryModelAssembler implements RepresentationModelAssembler<InventoryResponseDto, EntityModel<InventoryResponseDto>> {
    @Override
    public EntityModel<InventoryResponseDto> toModel(InventoryResponseDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(InventoryController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(InventoryController.class).all()).withRel("inventories"));
    }

    @Override
    public CollectionModel<EntityModel<InventoryResponseDto>> toCollectionModel(Iterable<? extends InventoryResponseDto> entities) {
        return RepresentationModelAssembler.super
                .toCollectionModel(entities)
                .add(linkTo(methodOn(InventoryController.class).all()).withSelfRel());
    }
}
