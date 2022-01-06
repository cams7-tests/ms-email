package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.port.in.exception.CommonExceptions.responseInternalServerErrorException;

import br.cams7.tests.ms.core.port.in.GetEmailsUseCase;
import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;
import br.cams7.tests.ms.core.port.out.GetEmailsRepository;
import br.cams7.tests.ms.core.port.pagination.OrderDTO;
import br.cams7.tests.ms.core.port.pagination.PageDTO;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetEmailsUseCaseImpl implements GetEmailsUseCase {

  private final ModelMapper modelMapper;
  private final GetEmailsRepository getEmailsRepository;

  @Override
  public Mono<PageDTO<EmailResponseDTO>> findAll(int page, int size, List<OrderDTO> orders) {
    return getEmailsRepository
        .findAll(page, size, orders)
        .map(this::getPageDTO)
        .switchIfEmpty(responseInternalServerErrorException());
  }

  private PageDTO<EmailResponseDTO> getPageDTO(PageDTO<EmailEntity> page) {
    @SuppressWarnings("unchecked")
    var anotherPage = (PageDTO<EmailResponseDTO>) modelMapper.map(page, PageDTO.class);
    anotherPage.setContent(
        page.getContent().stream().map(this::getEmailResponseDTO).collect(Collectors.toList()));
    return anotherPage;
  }

  private EmailResponseDTO getEmailResponseDTO(EmailEntity email) {
    EmailResponseDTO response = modelMapper.map(email, EmailResponseDTO.class);
    response.setIdentificationNumber(email.getOwnerRef());
    return response;
  }
}
