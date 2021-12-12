package br.cams7.tests.ms.infra.outbound.persistence;

import br.cams7.tests.ms.core.domain.Email;
import br.cams7.tests.ms.core.domain.PageInfo;
import br.cams7.tests.ms.core.ports.EmailRepositoryPort;
import br.cams7.tests.ms.infra.outbound.persistence.entities.EmailEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@Primary
public class PostgresEmailRepository implements EmailRepositoryPort {

  private final SpringDataPostgresEmailRepository emailRepository;

  public PostgresEmailRepository(final SpringDataPostgresEmailRepository orderRepository) {
    this.emailRepository = orderRepository;
  }

  @Autowired ModelMapper modelMapper;

  @Override
  public Email save(Email email) {
    EmailEntity emailEntity = emailRepository.save(modelMapper.map(email, EmailEntity.class));
    return modelMapper.map(emailEntity, Email.class);
  }

  @Override
  public List<Email> findAll(PageInfo pageInfo) {
    Pageable pageable = PageRequest.of(pageInfo.getPageNumber(), pageInfo.getPageSize());
    return emailRepository.findAll(pageable).stream()
        .map(entity -> modelMapper.map(entity, Email.class))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Email> findById(UUID emailId) {
    Optional<EmailEntity> emailEntity = emailRepository.findById(emailId);
    if (emailEntity.isPresent()) {
      return Optional.of(modelMapper.map(emailEntity.get(), Email.class));
    } else {
      return Optional.empty();
    }
  }
}
