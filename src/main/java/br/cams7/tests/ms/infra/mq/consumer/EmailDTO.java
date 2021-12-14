package br.cams7.tests.ms.infra.mq.consumer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EmailDTO {

  private String ownerRef;
  private String emailFrom;
  private String emailTo;
  private String subject;
  private String text;
}
