/** */
package br.cams7.tests.ms.core.service;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailToQueueUseCase;
import br.cams7.tests.ms.core.port.in.exception.InvalidIdentificationNumberException;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberGateway;
import br.cams7.tests.ms.core.port.out.SendEmailToQueueGateway;
import br.cams7.tests.ms.domain.EmailEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;

/** @author cams7 */
@RequiredArgsConstructor
public class SendEmailToQueueUseCaseImpl implements SendEmailToQueueUseCase {

  private final ModelMapper modelMapper;
  private final SendEmailToQueueGateway sendEmailServiceGateway;
  private final CheckIdentificationNumberGateway checkIdentificationNumberGateway;

  @Override
  public Mono<Void> execute(EmailVO vo) {
    return checkIdentificationNumberGateway
        .isValid(vo.getIdentificationNumber())
        .flatMap(
            isValid -> {
              if (!isValid)
                return Mono.error(
                    new InvalidIdentificationNumberException(vo.getIdentificationNumber()));

              EmailEntity email = modelMapper.map(vo, EmailEntity.class);
              email.setOwnerRef(vo.getIdentificationNumber());

              sendEmailServiceGateway.sendEmail(email);

              return Mono.empty();
            });
  }
}
