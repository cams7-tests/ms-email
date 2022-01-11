package br.cams7.tests.ms.infra.entrypoint.exception.validation;

import lombok.Data;

@Data
public class ValidationErrorDTO {
  private String defaultMessage;
  private String field;
}
