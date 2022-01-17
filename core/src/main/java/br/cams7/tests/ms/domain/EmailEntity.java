package br.cams7.tests.ms.domain;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.util.UUID;
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

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public void setEmailId(UUID emailId) {
    if (!isNull(emailId)) setEmailId(String.valueOf(emailId));
  }

  public void setEmailStatus(EmailStatusEnum emailStatus) {
    this.emailStatus = emailStatus;
  }

  public void setEmailStatus(byte emailStatus) {
    if (emailStatus >= 0 && emailStatus < EmailStatusEnum.values().length)
      setEmailStatus(EmailStatusEnum.values()[emailStatus]);
  }
}
