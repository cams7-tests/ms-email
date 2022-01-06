/** */
package br.cams7.tests.ms.core.port.in.presenter;

import br.cams7.tests.ms.domain.EmailStatusEnum;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class EmailResponseDTO {
  private UUID emailId;
  private String identificationNumber;
  private String emailFrom;
  private String emailTo;
  private String subject;
  private String text;
  private LocalDateTime emailSentDate;
  private EmailStatusEnum emailStatus;
}
