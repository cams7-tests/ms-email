package br.cams7.tests.ms.core.service;

import br.cams7.tests.ms.core.common.PageDTO;
import br.cams7.tests.ms.core.port.in.GetEmailsUseCase;
import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;
import br.cams7.tests.ms.core.port.out.GetEmailsRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@RequiredArgsConstructor
public class GetEmailsUseCaseImpl implements GetEmailsUseCase {

  private final ModelMapper modelMapper;
  private final GetEmailsRepository getEmailsRepository;

  @Override
  public List<EmailResponseDTO> findAll(PageDTO page) {
    // inserir manipulação de dados/regras
    return getEmailsRepository.findAll(page).stream()
        .map(this::getEmailResponseDTO)
        .collect(Collectors.toList());
  }

  private EmailResponseDTO getEmailResponseDTO(EmailEntity email) {
    EmailResponseDTO response = modelMapper.map(email, EmailResponseDTO.class);
    response.setIdentificationNumber(email.getOwnerRef());
    return response;
  }
}
