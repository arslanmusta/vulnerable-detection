package cve.inventoryservice.controller;

import cve.inventoryservice.model.error.ErrorMessageModel;
import cve.inventoryservice.model.error.ErrorModel;
import cve.inventoryservice.model.error.ErrorType;
import cve.inventoryservice.service.exception.InventoryItemNotFoundException;
import cve.inventoryservice.service.exception.InventoryNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class InventoryAdvice {

    @ResponseBody
    @ExceptionHandler(InventoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorModel> inventoryNotFoundHandler(InventoryNotFoundException ex) {
        return new ResponseEntity<>(createNotFoundErrorModel(ex.getId()), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(InventoryItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorModel> inventoryItemNotFoundHandler(InventoryItemNotFoundException ex) {
        return new ResponseEntity<>(createNotFoundErrorModel(ex.getId()), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorModel> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return createErrorModelResponseEntity(ex);
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorModel> handleConstraintViolation(ConstraintViolationException ex) {
        return createErrorModelResponseEntity(ex);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ErrorModel> handleException(Exception ex) {
        ErrorModel errorModel = new ErrorModel();
        errorModel.setDescription("Technical error");
        errorModel.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        errorModel.setErrorType(ErrorType.SERVER_ERROR);

        return new ResponseEntity<>(errorModel, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ErrorModel createNotFoundErrorModel(Object id) {
        ErrorModel errorModel = new ErrorModel();
        ErrorMessageModel errorMessageModel = new ErrorMessageModel();
        List<ErrorMessageModel> errorMessageModelList = new ArrayList<>();

        errorMessageModel.setField("id");
        errorMessageModel.setDescription(id.toString());

        errorMessageModelList.add(errorMessageModel);

        errorModel.setDescription(String.format("Entity not found with id = %s", id));
        errorModel.setStatus(HttpStatus.NOT_FOUND);
        errorModel.setErrorType(ErrorType.NOT_FOUND);
        errorModel.setErrors(errorMessageModelList);

        return errorModel;
    }

    private static ResponseEntity<ErrorModel> createErrorModelResponseEntity(MethodArgumentNotValidException ex) {
        ErrorModel errorModel = new ErrorModel();

        List<ErrorMessageModel> errorMessageModelList = ex.getBindingResult().getAllErrors().stream().map(objectError -> {
            ErrorMessageModel errorMessageModel = new ErrorMessageModel();

            errorMessageModel.setField(((FieldError) objectError).getField());
            errorMessageModel.setDescription(objectError.getDefaultMessage() != null ? objectError.getDefaultMessage() : "");

            return errorMessageModel;
        }).toList();

        errorModel.setDescription("Validation error");
        errorModel.setStatus(HttpStatus.BAD_REQUEST);
        errorModel.setErrorType(ErrorType.VALIDATION);
        errorModel.setErrors(errorMessageModelList);

        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
    }

    private static ResponseEntity<ErrorModel> createErrorModelResponseEntity(ConstraintViolationException ex) {
        ErrorModel errorModel = new ErrorModel();

        List<ErrorMessageModel> errorMessageModelList = ex.getConstraintViolations().stream().map(violation -> {
            ErrorMessageModel errorMessageModel = new ErrorMessageModel();

            errorMessageModel.setField(
                    StreamSupport
                            .stream(
                                    violation.getPropertyPath().spliterator(), false)
                            .reduce(
                                    "",
                                    (prev, next) -> next.getName(),
                                    (prev, next) -> next));

            errorMessageModel.setDescription(violation.getMessage() != null ? violation.getMessage() : "");

            return errorMessageModel;
        }).toList();

        errorModel.setDescription("Validation error");
        errorModel.setStatus(HttpStatus.BAD_REQUEST);
        errorModel.setErrorType(ErrorType.VALIDATION);
        errorModel.setErrors(errorMessageModelList);

        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
    }
}
