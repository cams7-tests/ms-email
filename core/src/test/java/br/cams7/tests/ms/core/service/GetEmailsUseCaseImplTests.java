package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.PAGE_NUMBER;
import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.PAGE_SIZE;
import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.getPageDTO;
import static br.cams7.tests.ms.core.port.pagination.SortDTOTestData.ORDER;
import static br.cams7.tests.ms.domain.EmailEntityTestData.getEmailEntity;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static reactor.test.StepVerifier.create;

import br.cams7.tests.ms.core.port.in.exception.ResponseStatusException;
import br.cams7.tests.ms.core.port.out.GetEmailsGateway;
import br.cams7.tests.ms.core.port.pagination.PageDTO;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@ExtendWith(MockitoExtension.class)
class GetEmailsUseCaseImplTests {

  private static final EmailEntity DEFAULT_EMAIL_ENTITY = getEmailEntity();
  private static final PageDTO<EmailEntity> PAGE_DTO_OF_ENTITY = getPageDTO(DEFAULT_EMAIL_ENTITY);

  @InjectMocks private GetEmailsUseCaseImpl getAllEmailsUseCase;

  @Mock private GetEmailsGateway getEmailsGateway;

  @BeforeAll
  static void blockHoundSetup() {
    BlockHound.install();
  }

  @Test
  void blockHoundWorks() {
    try {
      FutureTask<?> task =
          new FutureTask<>(
              () -> {
                Thread.sleep(0); // NOSONAR
                return "";
              });
      Schedulers.parallel().schedule(task);

      task.get(10, TimeUnit.SECONDS);
      fail("should fail");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof BlockingOperationError);
    }
  }

  @Test
  @DisplayName("findAll returns paged emails when successfull")
  void findAll_ReturnsPagedEmails_WhenSuccessful() {
    given(getEmailsGateway.findAll(anyInt(), anyInt(), anyList()))
        .willReturn(Mono.just(PAGE_DTO_OF_ENTITY));

    var orders = List.of(ORDER);

    create(getAllEmailsUseCase.execute(PAGE_NUMBER, PAGE_SIZE, orders))
        .expectSubscription()
        .expectNext(PAGE_DTO_OF_ENTITY)
        .verifyComplete();

    then(getEmailsGateway).should(times(1)).findAll(eq(PAGE_NUMBER), eq(PAGE_SIZE), eq(orders));
  }

  @Test
  @DisplayName("findAll returns error when empty is returned")
  void findAll_ReturnsError_WhenEmptyIsReturned() {
    given(getEmailsGateway.findAll(anyInt(), anyInt(), anyList())).willReturn(Mono.empty());

    var orders = List.of(ORDER);

    create(getAllEmailsUseCase.execute(PAGE_NUMBER, PAGE_SIZE, orders))
        .expectSubscription()
        .expectError(ResponseStatusException.class)
        .verify();

    then(getEmailsGateway).should(times(1)).findAll(eq(PAGE_NUMBER), eq(PAGE_SIZE), eq(orders));
  }

  @Test
  @DisplayName("findAll returns error when error is returned")
  void findAll_ReturnsError_WhenErrorIsReturned() {
    given(getEmailsGateway.findAll(anyInt(), anyInt(), anyList()))
        .willReturn(Mono.error(new RuntimeException()));

    var orders = List.of(ORDER);

    create(getAllEmailsUseCase.execute(PAGE_NUMBER, PAGE_SIZE, orders))
        .expectSubscription()
        .expectError(RuntimeException.class)
        .verify();

    then(getEmailsGateway).should(times(1)).findAll(eq(PAGE_NUMBER), eq(PAGE_SIZE), eq(orders));
  }
}
