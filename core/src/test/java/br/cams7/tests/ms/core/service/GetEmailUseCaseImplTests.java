package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_ID;
import static br.cams7.tests.ms.domain.EmailEntityTestData.getEmailEntity;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static reactor.test.StepVerifier.create;

import br.cams7.tests.ms.core.port.in.exception.ResponseStatusException;
import br.cams7.tests.ms.core.port.out.GetEmailGateway;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class GetEmailUseCaseImplTests {

  private static final EmailEntity DEFAULT_EMAIL_ENTITY = getEmailEntity();

  @InjectMocks private GetEmailUseCaseImpl getEmailUseCase;

  @Spy private ModelMapper modelMapper = new ModelMapper();
  @Mock private GetEmailGateway getEmailGateway;

  @Test
  @DisplayName("findById returns an email when successfull")
  void findById_ReturnsAnEmail_WhenSuccessful() {
    given(getEmailGateway.findById(any(UUID.class))).willReturn(Mono.just(DEFAULT_EMAIL_ENTITY));

    create(getEmailUseCase.execute(EMAIL_ID))
        .expectSubscription()
        .expectNext(DEFAULT_EMAIL_ENTITY)
        .verifyComplete();

    then(getEmailGateway).should(times(1)).findById(eq(EMAIL_ID));
  }

  @Test
  @DisplayName("findById returns error when empty is returned")
  void findById_ReturnsError_WhenEmptyIsReturned() {
    given(getEmailGateway.findById(any(UUID.class))).willReturn(Mono.empty());

    create(getEmailUseCase.execute(EMAIL_ID))
        .expectSubscription()
        .expectError(ResponseStatusException.class)
        .verify();

    then(getEmailGateway).should(times(1)).findById(eq(EMAIL_ID));
  }

  @Test
  @DisplayName("findById throws error when pass null email id")
  void findById_ThrowsError_WhenPassNullEmailId() {
    willThrow(new NullPointerException()).given(getEmailGateway).findById(null);

    assertThrows(
        NullPointerException.class,
        () -> {
          getEmailUseCase.execute(null);
        });
    then(getEmailGateway).should(times(1)).findById(null);
  }

  @Test
  @DisplayName("findById returns error when some error happened during get email")
  void findById_ReturnsError_WhenSomeErrorHappenedDuringGetEmail() {
    given(getEmailGateway.findById(any(UUID.class))).willReturn(Mono.error(new RuntimeException()));

    create(getEmailUseCase.execute(EMAIL_ID))
        .expectSubscription()
        .expectError(RuntimeException.class)
        .verify();

    then(getEmailGateway).should(times(1)).findById(eq(EMAIL_ID));
  }
}
