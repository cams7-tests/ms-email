package br.cams7.tests.ms.infra.entrypoint.mapper;

import br.cams7.tests.ms.core.port.pagination.PageDTO;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTO;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseConverter {

  private final ModelMapper modelMapper;

  @SuppressWarnings("unchecked")
  public PageDTO<EmailResponseDTO> convert(PageDTO<EmailEntity> page) {
    var anotherPage = modelMapper.map(page, PageDTO.class);
    anotherPage.setContent(
        page.getContent().stream().map(this::convert).collect(Collectors.toList()));
    return anotherPage;
  }

  public EmailResponseDTO convert(EmailEntity email) {
    EmailResponseDTO response = modelMapper.map(email, EmailResponseDTO.class);
    response.setIdentificationNumber(email.getOwnerRef());
    return response;
  }
}
