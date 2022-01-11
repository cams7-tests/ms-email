package br.cams7.tests.ms.core.port.in;

import br.cams7.tests.ms.core.port.in.common.SelfValidating;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.hibernate.validator.constraints.br.CPF;

@Value
@EqualsAndHashCode(callSuper = false)
public class EmailVO extends SelfValidating<EmailVO> {

  @NotBlank @CPF private String identificationNumber;
  @NotBlank @Email private String emailFrom;
  @NotBlank @Email private String emailTo;
  @NotBlank private String subject;
  @NotBlank private String text;

  public EmailVO(
      String identificationNumber, String emailFrom, String emailTo, String subject, String text) {
    super();
    this.identificationNumber = identificationNumber;
    this.emailFrom = emailFrom;
    this.emailTo = emailTo;
    this.subject = subject;
    this.text = text;
    super.validateSelf();
  }
}
