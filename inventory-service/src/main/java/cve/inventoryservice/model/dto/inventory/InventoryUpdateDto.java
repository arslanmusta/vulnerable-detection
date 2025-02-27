package cve.inventoryservice.model.dto.inventory;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class InventoryUpdateDto {
    @NotEmpty
    @Pattern(regexp = "^((\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.){3}(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])$")
    private String ip;

    @NotEmpty
    private String domain;
}
