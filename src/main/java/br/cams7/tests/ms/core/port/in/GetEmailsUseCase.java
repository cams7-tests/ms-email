package br.cams7.tests.ms.core.port.in;

import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;
import br.cams7.tests.ms.core.port.pagination.OrderDTO;
import br.cams7.tests.ms.core.port.pagination.PageDTO;
import java.util.List;
import reactor.core.publisher.Mono;

public interface GetEmailsUseCase {

  Mono<PageDTO<EmailResponseDTO>> findAll(int page, int size, List<OrderDTO> orders);
}
