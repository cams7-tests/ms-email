package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.PAGE_NUMBER;
import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.PAGE_SIZE;
import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.defaultPageDTO;
import static br.cams7.tests.ms.core.port.pagination.SortDTOTestData.ORDER;
import static br.cams7.tests.ms.domain.EmailEntityTestData.defaultEmailEntity;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static reactor.test.StepVerifier.create;

import br.cams7.tests.ms.core.port.in.exception.ResponseStatusException;
import br.cams7.tests.ms.core.port.out.GetEmailsRepository;
import br.cams7.tests.ms.core.port.pagination.PageDTO;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class GetEmailsUseCaseImplTests {

  private static final EmailEntity DEFAULT_EMAIL_ENTITY = defaultEmailEntity();
  private static final PageDTO<EmailEntity> PAGE_DTO_OF_ENTITY =
      defaultPageDTO(DEFAULT_EMAIL_ENTITY);

  @InjectMocks private GetEmailsUseCaseImpl getAllEmailsUseCase;

  @Mock private GetEmailsRepository getEmailsRepository;

  @Test
  @DisplayName("findAll returns paged emails when successfull")
  void findAll_ReturnsPagedEmails_WhenSuccessful() {
    given(getEmailsRepository.findAll(anyInt(), anyInt(), anyList()))
        .willReturn(Mono.just(PAGE_DTO_OF_ENTITY));

    var orders = List.of(ORDER);

    create(getAllEmailsUseCase.findAll(PAGE_NUMBER, PAGE_SIZE, orders))
        .expectSubscription()
        .expectNext(PAGE_DTO_OF_ENTITY)
        .verifyComplete();

    then(getEmailsRepository).should(times(1)).findAll(eq(PAGE_NUMBER), eq(PAGE_SIZE), eq(orders));
  }

  @Test
  @DisplayName("findAll returns error when empty is returned")
  void findAll_ReturnsError_WhenEmptyIsReturned() {
    given(getEmailsRepository.findAll(anyInt(), anyInt(), anyList())).willReturn(Mono.empty());

    var orders = List.of(ORDER);

    create(getAllEmailsUseCase.findAll(PAGE_NUMBER, PAGE_SIZE, orders))
        .expectSubscription()
        .expectError(ResponseStatusException.class)
        .verify();

    then(getEmailsRepository).should(times(1)).findAll(eq(PAGE_NUMBER), eq(PAGE_SIZE), eq(orders));
  }

  @Test
  @DisplayName("findAll returns error when error is returned")
  void findAll_ReturnsError_WhenErrorIsReturned() {
    given(getEmailsRepository.findAll(anyInt(), anyInt(), anyList()))
        .willReturn(Mono.error(new RuntimeException()));

    var orders = List.of(ORDER);

    create(getAllEmailsUseCase.findAll(PAGE_NUMBER, PAGE_SIZE, orders))
        .expectSubscription()
        .expectError(RuntimeException.class)
        .verify();

    then(getEmailsRepository).should(times(1)).findAll(eq(PAGE_NUMBER), eq(PAGE_SIZE), eq(orders));
  }
}
