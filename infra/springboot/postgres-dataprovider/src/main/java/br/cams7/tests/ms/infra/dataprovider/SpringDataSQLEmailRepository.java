package br.cams7.tests.ms.infra.dataprovider;

import br.cams7.tests.ms.infra.dataprovider.model.EmailModel;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SpringDataSQLEmailRepository extends ReactiveCrudRepository<EmailModel, UUID> {
  Flux<EmailModel> findAllBy(Pageable pageable);
}
