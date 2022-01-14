package br.cams7.tests.ms.infra.dataprovider;

import br.cams7.tests.ms.infra.dataprovider.model.UserModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SpringDataUserRepository extends ReactiveMongoRepository<UserModel, String> {
  Mono<UserModel> findByUsername(String username);
}
