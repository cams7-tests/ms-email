package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.port.in.exception.CommonExceptions.responseInternalServerErrorException;
import static java.lang.Boolean.TRUE;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import br.cams7.tests.ms.core.port.in.exception.InvalidIdentificationNumberException;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberGateway;
import br.cams7.tests.ms.core.port.out.SaveEmailGateway;
import br.cams7.tests.ms.core.port.out.SendEmailGateway;
import br.cams7.tests.ms.domain.EmailEntity;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SendEmailDirectlyUseCaseImpl implements SendEmailDirectlyUseCase {

  private static final String ERROR_MESSAGE = "An error occurred while trying to send email";

  private final ModelMapper modelMapper;
  private final SaveEmailGateway saveEmailGateway;
  private final SendEmailGateway sendEmailGateway;
  private final CheckIdentificationNumberGateway checkIdentificationNumberGateway;

  @Override
  public Mono<EmailEntity> execute(EmailVO email) {
    return checkIdentificationNumberGateway
        .isValid(email.getIdentificationNumber())
        .flatMap(
            isValid -> {
              if (!TRUE.equals(isValid))
                return Mono.error(
                    new InvalidIdentificationNumberException(email.getIdentificationNumber()));

              EmailEntity entity = modelMapper.map(email, EmailEntity.class);
              entity.setOwnerRef(email.getIdentificationNumber());

              entity.setEmailSentDate(LocalDateTime.now());

              return sendEmailGateway
                  .sendEmail(entity)
                  .flatMap(
                      emailStatus -> {
                        entity.setEmailStatus(emailStatus);
                        return save(entity);
                      });
            })
        .switchIfEmpty(responseInternalServerErrorException(ERROR_MESSAGE));
  }

  private Mono<EmailEntity> save(EmailEntity email) {
    return saveEmailGateway.save(email);
  }
}
