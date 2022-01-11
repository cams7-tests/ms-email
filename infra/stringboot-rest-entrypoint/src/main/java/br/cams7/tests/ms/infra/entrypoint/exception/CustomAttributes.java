package br.cams7.tests.ms.infra.entrypoint.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import br.cams7.tests.ms.core.port.in.exception.InvalidIdentificationNumberException;
import br.cams7.tests.ms.core.port.in.exception.ResponseStatusException;
import br.cams7.tests.ms.infra.entrypoint.exception.validation.ValidationErrorDTO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class CustomAttributes extends DefaultErrorAttributes {
  private static final String MESSAGE_ATTRIBUTE = "message";
  private static final String EXCEPTION_ATTRIBUTE = "exception";
  private static final String STATUS_ATTRIBUTE = "status";
  private static final String ERROR_ATTRIBUTE = "error";
  private static final String ERRORS_ATTRIBUTE = "errors";
  private static final String TRACE_ATTRIBUTE = "trace";

  private static final String ERROR_MESSAGE_SEPARATOR = ",";
  private static final String ATTRIBUTE_SEPARATOR = ":";

  private static final String IDENTIFICATION_NUMBER_FIELD = "identificationNumber";

  @Override
  public Map<String, Object> getErrorAttributes(
      ServerRequest request, ErrorAttributeOptions options) {
    var errorAttributes = super.getErrorAttributes(request, options);
    var throwable = getError(request);
    if (throwable instanceof ResponseStatusException) {
      setResponseStatusException(errorAttributes, throwable);
    } else if (throwable instanceof ConstraintViolationException) {
      setConstraintViolationException(errorAttributes, throwable);
    } else if (throwable instanceof InvalidIdentificationNumberException) {
      setInvalidIdentificationNumberException(errorAttributes, throwable);
    } else if (throwable instanceof DataIntegrityViolationException) {
      var exception = (DataIntegrityViolationException) throwable;
      errorAttributes.put(MESSAGE_ATTRIBUTE, exception.getLocalizedMessage());
      errorAttributes.put(EXCEPTION_ATTRIBUTE, DataIntegrityViolationException.class.getName());
    }
    return errorAttributes;
  }

  private static void setResponseStatusException(
      Map<String, Object> errorAttributes, Throwable throwable) {
    var exception = (ResponseStatusException) throwable;
    if (NOT_FOUND.value() == exception.getStatus()) {
      errorAttributes.put(STATUS_ATTRIBUTE, NOT_FOUND.value());
      errorAttributes.put(ERROR_ATTRIBUTE, NOT_FOUND.name());
      errorAttributes.put(TRACE_ATTRIBUTE, null);
    } else if (INTERNAL_SERVER_ERROR.value() == exception.getStatus()) {
      errorAttributes.put(STATUS_ATTRIBUTE, INTERNAL_SERVER_ERROR.value());
      errorAttributes.put(ERROR_ATTRIBUTE, INTERNAL_SERVER_ERROR.name());
      errorAttributes.put(TRACE_ATTRIBUTE, null);
    }
    errorAttributes.put(MESSAGE_ATTRIBUTE, exception.getMessage());
    errorAttributes.put(EXCEPTION_ATTRIBUTE, ResponseStatusException.class.getName());
  }

  private static void setConstraintViolationException(
      Map<String, Object> errorAttributes, Throwable throwable) {
    var exception = (ConstraintViolationException) throwable;
    errorAttributes.put(STATUS_ATTRIBUTE, BAD_REQUEST.value());
    errorAttributes.put(ERROR_ATTRIBUTE, BAD_REQUEST.name());
    errorAttributes.put(ERRORS_ATTRIBUTE, getValidationErrors(exception.getMessage()));
    errorAttributes.put(TRACE_ATTRIBUTE, null);
    errorAttributes.put(EXCEPTION_ATTRIBUTE, ConstraintViolationException.class.getName());
  }

  private static void setInvalidIdentificationNumberException(
      Map<String, Object> errorAttributes, Throwable throwable) {
    var exception = (InvalidIdentificationNumberException) throwable;
    errorAttributes.put(STATUS_ATTRIBUTE, BAD_REQUEST.value());
    errorAttributes.put(ERROR_ATTRIBUTE, BAD_REQUEST.name());
    errorAttributes.put(
        ERRORS_ATTRIBUTE, getValidationError(IDENTIFICATION_NUMBER_FIELD, exception.getMessage()));
    errorAttributes.put(TRACE_ATTRIBUTE, null);
    errorAttributes.put(EXCEPTION_ATTRIBUTE, InvalidIdentificationNumberException.class.getName());
  }

  private static List<ValidationErrorDTO> getValidationErrors(String errorMessage) {
    return List.of(errorMessage.split(ERROR_MESSAGE_SEPARATOR)).stream()
        .map(CustomAttributes::getValidationError)
        .collect(Collectors.toList());
  }

  private static ValidationErrorDTO getValidationError(String errorMessage) {
    var atributeWithMessage = errorMessage.split(ATTRIBUTE_SEPARATOR);
    var error = new ValidationErrorDTO();
    error.setField(atributeWithMessage[0].trim());
    error.setDefaultMessage(atributeWithMessage[1].trim());
    return error;
  }

  private static List<ValidationErrorDTO> getValidationError(String field, String errorMessage) {
    var error = new ValidationErrorDTO();
    error.setField(field);
    error.setDefaultMessage(errorMessage);
    return List.of(error);
  }
}
