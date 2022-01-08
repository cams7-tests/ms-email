package br.cams7.tests.ms.infra.dataprovider;

import br.cams7.tests.ms.infra.logging.LogEntryExit;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements ReactiveUserDetailsService {

  private final SpringDataSQLUserRepository repository;

  @LogEntryExit(showArgs = true, showResult = true)
  @Override
  public Mono<UserDetails> findByUsername(String username) {
    return repository.findByUsername(username).cast(UserDetails.class);
  }
}
