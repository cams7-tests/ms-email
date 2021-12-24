package br.cams7.tests.ms.infra.persistence.repository;

import br.cams7.tests.ms.core.common.PageDTO;
import br.cams7.tests.ms.core.port.out.GetEmailRepository;
import br.cams7.tests.ms.core.port.out.GetEmailsRepository;
import br.cams7.tests.ms.core.port.out.SaveEmailRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.infra.persistence.model.EmailModel;
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
public class SQLEmailRepository
    implements GetEmailsRepository, GetEmailRepository, SaveEmailRepository {

  private final ModelMapper modelMapper;
  private final SpringDataSQLEmailRepository emailRepository;

  @Autowired
  SQLEmailRepository(ModelMapper modelMapper, SpringDataSQLEmailRepository emailRepository) {
    super();
    this.modelMapper = modelMapper;
    this.emailRepository = emailRepository;
  }

  @Override
  public EmailEntity save(EmailEntity entity) {
    EmailModel model = emailRepository.save(modelMapper.map(entity, EmailModel.class));
    return modelMapper.map(model, EmailEntity.class);
  }

  @Override
  public List<EmailEntity> findAll(PageDTO page) {
    Pageable pageable = PageRequest.of(page.getPageNumber(), page.getPageSize());
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
