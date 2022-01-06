package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.port.in.exception.CommonExceptions.responseNotFoundException;

import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;
import br.cams7.tests.ms.core.port.out.GetEmailRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetEmailUseCaseImpl implements GetEmailUseCase {

  private final ModelMapper modelMapper;
  private final GetEmailRepository getEmailRepository;

  @Override
  public Mono<EmailResponseDTO> findById(UUID emailId) {
    return getEmailRepository
        .findById(emailId)
        .map(this::getEmailResponseDTO)
        .switchIfEmpty(responseNotFoundException());
  }

  private EmailResponseDTO getEmailResponseDTO(EmailEntity email) {
    EmailResponseDTO response = modelMapper.map(email, EmailResponseDTO.class);
    response.setIdentificationNumber(email.getOwnerRef());
    return response;
  }
}
