package br.cams7.tests.ms.infra.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EmailRequestDTO {

  private String identificationNumber;
  private String emailFrom;
  private String emailTo;
  private String subject;
  private String text;
}
