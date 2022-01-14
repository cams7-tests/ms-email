package br.cams7.tests.ms.infra.dataprovider;

import br.cams7.tests.ms.infra.dataprovider.model.EmailModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SpringDataEmailRepository extends ReactiveMongoRepository<EmailModel, String> {

  Flux<EmailModel> findAllBy(Pageable pageable);
}
