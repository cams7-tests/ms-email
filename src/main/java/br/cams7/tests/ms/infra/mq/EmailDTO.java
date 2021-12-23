package br.cams7.tests.ms.infra.mq;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EmailDTO {

  private String ownerRef;
  private String emailFrom;
  private String emailTo;
  private String subject;
  private String text;
}
