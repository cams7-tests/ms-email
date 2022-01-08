package br.cams7.tests.ms.infra.entrypoint;

import static org.springframework.data.domain.Sort.Direction.DESC;

import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.in.GetEmailsUseCase;
import br.cams7.tests.ms.core.port.pagination.OrderDTO;
import br.cams7.tests.ms.core.port.pagination.PageDTO;
import br.cams7.tests.ms.infra.entrypoint.mapper.ResponseConverter;
import br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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

@SecurityScheme(
    name = GetEmailController.SECURITY_SCHEME_NAME,
    type = SecuritySchemeType.HTTP,
    scheme = GetEmailController.SECURITY_SCHEME_SCHEME)
@RestController
@RequiredArgsConstructor
public class GetEmailController {

  public static final String SECURITY_SCHEME_NAME = "Basic Authentication";
  public static final String SECURITY_SCHEME_SCHEME = "basic";
  public static final String OPERATION_TAGS = "ms-email";

  private static final int DEFAULT_PAGE_NUMBER = 0;
  private static final int DEFAULT_PAGE_SIZE = 5;
  private static final String DEFAULT_SORT_FIELD = "emailId";

  private final ModelMapper modelMapper;
  private final GetEmailsUseCase getAllEmailsUseCase;
  private final GetEmailUseCase getEmailUseCase;
  private final ResponseConverter responseConverter;

  @Operation(
      summary = "List emails",
      tags = {OPERATION_TAGS},
      security = @SecurityRequirement(name = SECURITY_SCHEME_NAME))
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

  @Operation(
      summary = "Get the email by id",
      tags = {OPERATION_TAGS},
      security = @SecurityRequirement(name = SECURITY_SCHEME_NAME))
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
