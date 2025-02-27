package cve.inventoryservice.model.error;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class ErrorModel {
    private HttpStatus status;

    private String description;

    private ErrorType errorType;

    private List<ErrorMessageModel> errors;
}
