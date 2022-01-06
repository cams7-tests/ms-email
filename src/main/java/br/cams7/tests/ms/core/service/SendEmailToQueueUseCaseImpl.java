/** */
package br.cams7.tests.ms.core.service;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailToQueueUseCase;
import br.cams7.tests.ms.core.port.in.exception.InvalidIdentificationNumberException;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberService;
import br.cams7.tests.ms.core.port.out.SendEmailToQueueService;
import br.cams7.tests.ms.domain.EmailEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;

/** @author cams7 */
@RequiredArgsConstructor
public class SendEmailToQueueUseCaseImpl implements SendEmailToQueueUseCase {

  private final ModelMapper modelMapper;
  private final SendEmailToQueueService sendEmailService;
  private final CheckIdentificationNumberService checkIdentificationNumberService;

  @Override
  public Mono<Void> sendEmail(EmailVO vo) {
    return checkIdentificationNumberService
        .isValid(vo.getIdentificationNumber())
        .flatMap(
            isValid -> {
              if (!isValid)
                return Mono.error(
                    new InvalidIdentificationNumberException(vo.getIdentificationNumber()));

              EmailEntity email = modelMapper.map(vo, EmailEntity.class);
              email.setOwnerRef(vo.getIdentificationNumber());

              sendEmailService.sendEmail(email);

              return Mono.empty();
            });
  }
}
