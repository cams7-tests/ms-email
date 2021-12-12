package br.cams7.tests.ms.core.domain;

import br.cams7.tests.ms.core.common.SelfValidating;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
public class EmailEntity extends SelfValidating<EmailEntity> {

  private UUID emailId;
  @NotBlank private String ownerRef;
  @NotBlank @Email private String emailFrom;
  @NotBlank @Email private String emailTo;
  @NotBlank private String subject;
  @NotBlank private String text;
  private LocalDateTime sendDateEmail;
  private StatusEmail statusEmail;
}
