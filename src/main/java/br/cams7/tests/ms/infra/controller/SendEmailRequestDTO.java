package br.cams7.tests.ms.infra.controller;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class SendEmailRequestDTO {

  private String identificationNumber;
  private String emailFrom;
  private String emailTo;
  private String subject;
  private String text;
}
