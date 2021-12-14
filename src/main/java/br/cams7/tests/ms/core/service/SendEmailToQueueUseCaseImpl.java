/** */
package br.cams7.tests.ms.core.service;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailToQueueUseCase;
import br.cams7.tests.ms.core.port.in.exception.InvalidIdentificationNumberException;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberService;
import br.cams7.tests.ms.core.port.out.SendEmailToQueueService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

/** @author cams7 */
@RequiredArgsConstructor
public class SendEmailToQueueUseCaseImpl implements SendEmailToQueueUseCase {

  private static final ModelMapper MODEL_MAPPER = new ModelMapper();

  private final SendEmailToQueueService sendEmailService;
  private final CheckIdentificationNumberService checkIdentificationNumberService;

  @Override
  public void sendEmail(EmailVO vo) {
    if (!checkIdentificationNumberService.isValid(vo.getIdentificationNumber()))
      throw new InvalidIdentificationNumberException(vo.getIdentificationNumber());

    EmailEntity email = MODEL_MAPPER.map(vo, EmailEntity.class);
    email.setOwnerRef(vo.getIdentificationNumber());

    sendEmailService.sendEmail(email);
  }
}
