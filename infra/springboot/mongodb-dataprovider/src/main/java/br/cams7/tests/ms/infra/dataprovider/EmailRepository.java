package br.cams7.tests.ms.infra.dataprovider;

import br.cams7.tests.ms.core.port.out.GetEmailGateway;
import br.cams7.tests.ms.core.port.out.GetEmailsGateway;
import br.cams7.tests.ms.core.port.out.SaveEmailGateway;
import br.cams7.tests.ms.core.port.pagination.OrderDTO;
import br.cams7.tests.ms.core.port.pagination.PageDTO;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.infra.dataprovider.model.EmailModel;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EmailRepository implements GetEmailsGateway, GetEmailGateway, SaveEmailGateway {

  private final ModelMapper modelMapper;
  private final SpringDataEmailRepository emailRepository;

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
    return emailRepository.findById(emailId).map(this::getEmailEntity);
  }

  private EmailModel getEmailModel(EmailEntity email) {
    var model = modelMapper.map(email, EmailModel.class);
    model.setId(email.getEmailId());
    return model;
  }

  private EmailEntity getEmailEntity(EmailModel email) {
    var entity = modelMapper.map(email, EmailEntity.class);
    entity.setEmailId(email.getId());
    return entity;
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
