package br.cams7.tests.ms.core.port.out;

import br.cams7.tests.ms.core.port.pagination.OrderDTO;
import br.cams7.tests.ms.core.port.pagination.PageDTO;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.List;
import reactor.core.publisher.Mono;

public interface GetEmailsGateway {

  Mono<PageDTO<EmailEntity>> findAll(int page, int size, List<OrderDTO> orders);
}
