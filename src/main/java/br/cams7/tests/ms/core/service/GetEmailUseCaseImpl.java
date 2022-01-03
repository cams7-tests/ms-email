package br.cams7.tests.ms.core.service;

import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;
import br.cams7.tests.ms.core.port.out.GetEmailRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@RequiredArgsConstructor
public class GetEmailUseCaseImpl implements GetEmailUseCase {

  private final ModelMapper modelMapper;
  private final GetEmailRepository getEmailRepository;

  @Override
  public Optional<EmailResponseDTO> findById(UUID emailId) {
    // inserir manipulação de dados/regras
    return getEmailResponseDTO(getEmailRepository.findById(emailId));
  }

  private Optional<EmailResponseDTO> getEmailResponseDTO(Optional<EmailEntity> email) {
    if (email.isPresent()) {
      EmailResponseDTO response = modelMapper.map(email.get(), EmailResponseDTO.class);
      response.setIdentificationNumber(email.get().getOwnerRef());
      return Optional.of(response);
    }
    return Optional.empty();
  }
}
