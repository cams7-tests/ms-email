package br.cams7.tests.ms.infra.mq;

import lombok.Data;

@Data
public class EmailDTO {

  private String ownerRef;
  private String emailFrom;
  private String emailTo;
  private String subject;
  private String text;
}
