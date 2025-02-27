package cve.inventoryservice.model.assembler;

import cve.inventoryservice.controller.InventoryItemController;
import cve.inventoryservice.model.dto.inventoryitem.InventoryItemResponseDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Iterator;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@SuppressWarnings("NullableProblems")
@Component
public class InventoryItemItemModelAssembler implements RepresentationModelAssembler<InventoryItemResponseDto, EntityModel<InventoryItemResponseDto>> {
    @Override
    public EntityModel<InventoryItemResponseDto> toModel(InventoryItemResponseDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(InventoryItemController.class).one(entity.getInventoryId(), entity.getId())).withSelfRel(),
                linkTo(methodOn(InventoryItemController.class).all(entity.getInventoryId())).withRel("inventoryItems"));
    }

    @Override
    public CollectionModel<EntityModel<InventoryItemResponseDto>> toCollectionModel(Iterable<? extends InventoryItemResponseDto> entities) {
        int id = -1;

        Iterator<? extends InventoryItemResponseDto> iterator = entities.iterator();

        if (iterator.hasNext()) {
            id = iterator.next().getInventoryId();
        }

        return RepresentationModelAssembler.super
                .toCollectionModel(entities)
                .add(linkTo(methodOn(InventoryItemController.class).all(id)).withSelfRel());
    }

}
