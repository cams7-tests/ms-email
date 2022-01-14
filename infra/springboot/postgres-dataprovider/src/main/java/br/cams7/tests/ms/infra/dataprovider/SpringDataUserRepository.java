package br.cams7.tests.ms.infra.dataprovider;

import br.cams7.tests.ms.infra.dataprovider.model.UserModel;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SpringDataUserRepository extends ReactiveCrudRepository<UserModel, UUID> {
  Mono<UserModel> findByUsername(String username);
}
