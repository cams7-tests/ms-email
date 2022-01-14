package br.cams7.tests.ms.infra.dataprovider;

import static org.springframework.util.ObjectUtils.isEmpty;

import br.cams7.tests.ms.core.port.out.GetEmailGateway;
import br.cams7.tests.ms.core.port.out.GetEmailsGateway;
import br.cams7.tests.ms.core.port.out.SaveEmailGateway;
import br.cams7.tests.ms.core.port.pagination.OrderDTO;
import br.cams7.tests.ms.core.port.pagination.PageDTO;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailStatusEnum;
import br.cams7.tests.ms.infra.dataprovider.model.EmailModel;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EmailRepository implements GetEmailsGateway, GetEmailGateway, SaveEmailGateway {

  private final ModelMapper modelMapper;
  private final SpringDataEmailRepository emailRepository;

  EmailRepository(ModelMapper modelMapper, SpringDataEmailRepository emailRepository) {
    super();
    this.modelMapper = modelMapper;
    this.emailRepository = emailRepository;
    addMappings();
  }

  private void addMappings() {
    modelMapper.addMappings(
        new PropertyMap<EmailEntity, EmailModel>() {
          @Override
          protected void configure() {
            skip(destination.getEmailStatus());
            skip(destination.getEmailId());
          }
        });
  }

  @Override
  public Mono<EmailEntity> save(EmailEntity email) {
    return emailRepository.save(getEmailModel(email)).map(this::getEmailEntity);
  }

  @Override
  public Mono<PageDTO<EmailEntity>> findAll(int page, int size, List<OrderDTO> orders) {
    var pageable = PageRequest.of(page, size, Sort.by(getOrders(orders)));

    return emailRepository
        .findAllBy(pageable)
        .collectList()
        .zipWith(emailRepository.count())
        .map(tuple2 -> new PageImpl<>(tuple2.getT1(), pageable, tuple2.getT2()))
        .map(this::getPageDTO);
  }

  @Override
  public Mono<EmailEntity> findById(String emailId) {
    return emailRepository.findById(UUID.fromString(emailId)).map(this::getEmailEntity);
  }

  private EmailModel getEmailModel(EmailEntity email) {
    EmailModel model = modelMapper.map(email, EmailModel.class);
    model.setEmailStatus(getIndexOfEmailStatus(email.getEmailStatus()));
    model.setEmailId(!isEmpty(email.getEmailId()) ? UUID.fromString(email.getEmailId()) : null);
    return model;
  }

  private static byte getIndexOfEmailStatus(EmailStatusEnum emailStatus) {
    return (byte) emailStatus.ordinal();
  }

  private EmailEntity getEmailEntity(EmailModel email) {
    EmailEntity entity = modelMapper.map(email, EmailEntity.class);
    entity.setEmailStatus(getEmailStatusByIndex(email.getEmailStatus()));
    return entity;
  }

  private static EmailStatusEnum getEmailStatusByIndex(byte index) {
    return EmailStatusEnum.values()[index];
  }

  @SuppressWarnings("unchecked")
  private PageDTO<EmailEntity> getPageDTO(Page<EmailModel> page) {
    var anotherPage = modelMapper.map(page, PageDTO.class);
    anotherPage.setContent(
        page.getContent().stream().map(this::getEmailEntity).collect(Collectors.toList()));
    anotherPage.setHasContent(page.hasContent());
    anotherPage.setHasNext(page.hasNext());
    anotherPage.setHasPrevious(page.hasPrevious());
    if (anotherPage.getSort().isSorted()) {
      anotherPage
          .getSort()
          .setOrders(
              page.getSort().toList().stream()
                  .map(order -> modelMapper.map(order, OrderDTO.class))
                  .collect(Collectors.toList()));
    }
    return anotherPage;
  }

  private List<Order> getOrders(List<OrderDTO> orders) {
    return orders.stream()
        .map(
            order ->
                order.getDirection().isDescending()
                    ? Order.desc(order.getProperty())
                    : Order.asc(order.getProperty()))
        .collect(Collectors.toList());
  }
}
