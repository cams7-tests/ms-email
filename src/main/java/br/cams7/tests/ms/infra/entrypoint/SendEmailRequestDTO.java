package br.cams7.tests.ms.infra.entrypoint;

import lombok.Data;

@Data
public class SendEmailRequestDTO {

  private String identificationNumber;
  private String emailFrom;
  private String emailTo;
  private String subject;
  private String text;
}
