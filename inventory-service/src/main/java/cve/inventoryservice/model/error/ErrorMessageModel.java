package cve.inventoryservice.model.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessageModel {
    private String field;

    private String description;
}
