/** */
package br.cams7.tests.ms.infra.entrypoint.response;

import br.cams7.tests.ms.domain.EmailStatusEnum;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EmailResponseDTO {
  private String emailId;
  private String identificationNumber;
  private String emailFrom;
  private String emailTo;
  private String subject;
  private String text;
  private LocalDateTime emailSentDate;
  private EmailStatusEnum emailStatus;
}
