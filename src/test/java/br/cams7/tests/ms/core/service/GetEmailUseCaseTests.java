package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_ID;
import static br.cams7.tests.ms.domain.EmailEntityTestData.defaultEmailEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;

import br.cams7.tests.ms.core.port.out.GetEmailRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetEmailUseCaseTests {

  private static final EmailEntity DEFAULT_EMAIL_ENTITY = defaultEmailEntity();
  private static final String ERROR_MESSAGE = "Error";

  @InjectMocks private GetEmailUseCaseImpl getEmailUseCase;

  @Mock private GetEmailRepository getEmailRepository;

  @Test
  @DisplayName("findById returns an email when successfull")
  void findById_ReturnsAnEmail_WhenSuccessful() {
    given(getEmailRepository.findById(any(UUID.class)))
        .willReturn(Optional.of(DEFAULT_EMAIL_ENTITY));

    var email = getEmailUseCase.findById(EMAIL_ID);

    assertThat(email).contains(DEFAULT_EMAIL_ENTITY);

    then(getEmailRepository).should(times(1)).findById(eq(EMAIL_ID));
  }

  @Test
  @DisplayName("findById throws error when pass null email id")
  void findById_ThrowsError_WhenPassNullEmailId() {
    willThrow(new NullPointerException()).given(getEmailRepository).findById(null);

    assertThrows(
        NullPointerException.class,
        () -> {
          getEmailUseCase.findById(null);
        });
    then(getEmailRepository).should(times(1)).findById(null);
  }

  @Test
  @DisplayName("findById throws error when empty don't find email")
  void findById_ThrowsError_WhenDontFindEmail() {
    willThrow(new RuntimeException(ERROR_MESSAGE))
        .given(getEmailRepository)
        .findById(any(UUID.class));

    var thrown =
        assertThrows(
            RuntimeException.class,
            () -> {
              getEmailUseCase.findById(EMAIL_ID);
            });

    assertThat(thrown.getMessage()).isEqualTo(ERROR_MESSAGE);
    then(getEmailRepository).should(times(1)).findById(eq(EMAIL_ID));
  }
}
