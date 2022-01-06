package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.port.in.exception.CommonExceptions.responseInternalServerErrorException;

import br.cams7.tests.ms.core.port.in.GetEmailsUseCase;
import br.cams7.tests.ms.core.port.out.GetEmailsGateway;
import br.cams7.tests.ms.core.port.pagination.OrderDTO;
import br.cams7.tests.ms.core.port.pagination.PageDTO;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetEmailsUseCaseImpl implements GetEmailsUseCase {

  private final GetEmailsGateway getEmailsGateway;

  @Override
  public Mono<PageDTO<EmailEntity>> execute(int page, int size, List<OrderDTO> orders) {
    return getEmailsGateway
        .findAll(page, size, orders)
        .switchIfEmpty(responseInternalServerErrorException());
  }
}
