package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.port.in.exception.CommonExceptions.responseInternalServerErrorException;
import static br.cams7.tests.ms.domain.EmailStatusEnum.ERROR;
import static br.cams7.tests.ms.domain.EmailStatusEnum.SENT;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import br.cams7.tests.ms.core.port.in.exception.InvalidIdentificationNumberException;
import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberService;
import br.cams7.tests.ms.core.port.out.SaveEmailRepository;
import br.cams7.tests.ms.core.port.out.SendEmailService;
import br.cams7.tests.ms.core.port.out.exception.SendEmailException;
import br.cams7.tests.ms.domain.EmailEntity;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SendEmailDirectlyUseCaseImpl implements SendEmailDirectlyUseCase {

  private final ModelMapper modelMapper;
  private final SaveEmailRepository saveEmailRepository;
  private final SendEmailService sendEmailService;
  private final CheckIdentificationNumberService checkIdentificationNumberService;

  @Override
  public Mono<EmailResponseDTO> sendEmail(EmailVO email) {
    return checkIdentificationNumberService
        .isValid(email.getIdentificationNumber())
        .flatMap(
            isValid -> {
              if (!isValid)
                return Mono.error(
                    new InvalidIdentificationNumberException(email.getIdentificationNumber()));

              EmailEntity entity = modelMapper.map(email, EmailEntity.class);
              entity.setOwnerRef(email.getIdentificationNumber());

              entity.setEmailSentDate(LocalDateTime.now());

              Mono<EmailResponseDTO> newEmail = Mono.empty();
              try {
                sendEmailService.sendEmail(entity);
                entity.setEmailStatus(SENT);
              } catch (SendEmailException e) {
                entity.setEmailStatus(ERROR);
              } finally {
                newEmail = save(entity);
              }
              return newEmail;
            })
        .switchIfEmpty(responseInternalServerErrorException());
  }

  private Mono<EmailResponseDTO> save(EmailEntity email) {
    return saveEmailRepository.save(email).map(this::getEmailResponseDTO);
  }

  private EmailResponseDTO getEmailResponseDTO(EmailEntity email) {
    EmailResponseDTO response = modelMapper.map(email, EmailResponseDTO.class);
    response.setIdentificationNumber(email.getOwnerRef());
    return response;
  }
}
