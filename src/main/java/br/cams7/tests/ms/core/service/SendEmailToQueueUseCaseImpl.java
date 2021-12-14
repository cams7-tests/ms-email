/** */
package br.cams7.tests.ms.core.service;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailToQueueUseCase;
import br.cams7.tests.ms.core.port.out.SendEmailToQueueService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

/** @author cams7 */
@RequiredArgsConstructor
public class SendEmailToQueueUseCaseImpl implements SendEmailToQueueUseCase {

  private static final ModelMapper MODEL_MAPPER = new ModelMapper();

  private final SendEmailToQueueService sendEmailService;

  @Override
  public void sendEmail(EmailVO vo) {
    EmailEntity email = MODEL_MAPPER.map(vo, EmailEntity.class);
    sendEmailService.sendEmail(email);
  }
}
