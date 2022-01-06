package br.cams7.tests.ms.infra.entrypoint;

import static org.springframework.data.domain.Sort.Direction.DESC;

import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.in.GetEmailsUseCase;
import br.cams7.tests.ms.core.port.pagination.OrderDTO;
import br.cams7.tests.ms.core.port.pagination.PageDTO;
import br.cams7.tests.ms.infra.entrypoint.mapper.ResponseConverter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class GetEmailController {

  private static final int DEFAULT_PAGE_NUMBER = 0;
  private static final int DEFAULT_PAGE_SIZE = 5;
  private static final String DEFAULT_SORT_FIELD = "emailId";

  private final ModelMapper modelMapper;
  private final GetEmailsUseCase getAllEmailsUseCase;
  private final GetEmailUseCase getEmailUseCase;
  private final ResponseConverter responseConverter;

  @GetMapping(path = "/emails")
  @ResponseStatus(HttpStatus.OK)
  Mono<PageDTO<EmailResponseDTO>> getEmails(
      @PageableDefault(
              page = DEFAULT_PAGE_NUMBER,
              size = DEFAULT_PAGE_SIZE,
              sort = DEFAULT_SORT_FIELD,
              direction = DESC)
          Pageable pageable) {
    return getAllEmailsUseCase
        .execute(pageable.getPageNumber(), pageable.getPageSize(), getOrders(pageable.getSort()))
        .map(responseConverter::convert);
  }

  @GetMapping(path = "/emails/{emailId}")
  @ResponseStatus(HttpStatus.OK)
  Mono<EmailResponseDTO> getEmail(@PathVariable(value = "emailId") final UUID emailId) {
    return getEmailUseCase.execute(emailId).map(responseConverter::convert);
  }

  private List<OrderDTO> getOrders(Sort sort) {
    if (sort.isUnsorted()) return List.of();

    return sort.toList().stream()
        .map(order -> modelMapper.map(order, OrderDTO.class))
        .collect(Collectors.toList());
  }
}
