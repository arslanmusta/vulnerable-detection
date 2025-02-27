package cve.inventoryservice.model.dto.inventoryitem;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class InventoryItemCreationDto {
    @NotEmpty
    private String vendor;

    @NotEmpty
    private String product;

    @NotEmpty
    private String version;
}
