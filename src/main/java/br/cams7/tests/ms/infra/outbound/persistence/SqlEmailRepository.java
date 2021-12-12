package br.cams7.tests.ms.infra.outbound.persistence;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.domain.PageInfo;
import br.cams7.tests.ms.core.ports.EmailRepositoryPort;
import br.cams7.tests.ms.infra.outbound.persistence.models.EmailModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Primary
public class SqlEmailRepository implements EmailRepositoryPort {

  private final SpringDataSqlEmailRepository emailRepository;

  @Autowired private ModelMapper modelMapper;

  @Override
  public EmailEntity save(EmailEntity entity) {
    EmailModel model = emailRepository.save(modelMapper.map(entity, EmailModel.class));
    return modelMapper.map(model, EmailEntity.class);
  }

  @Override
  public List<EmailEntity> findAll(PageInfo pageInfo) {
    Pageable pageable = PageRequest.of(pageInfo.getPageNumber(), pageInfo.getPageSize());
    return emailRepository.findAll(pageable).stream()
        .map(model -> modelMapper.map(model, EmailEntity.class))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<EmailEntity> findById(UUID emailId) {
    Optional<EmailModel> model = emailRepository.findById(emailId);
    if (model.isPresent()) {
      return Optional.of(modelMapper.map(model.get(), EmailEntity.class));
    } else {
      return Optional.empty();
    }
  }
}
