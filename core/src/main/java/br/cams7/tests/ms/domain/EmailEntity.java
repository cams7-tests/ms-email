package br.cams7.tests.ms.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@EqualsAndHashCode(callSuper = false)
public class EmailEntity {

  private String emailId;
  private String ownerRef;
  private String emailFrom;
  private String emailTo;
  private String subject;
  private String text;
  private LocalDateTime emailSentDate;
  private EmailStatusEnum emailStatus;
}
