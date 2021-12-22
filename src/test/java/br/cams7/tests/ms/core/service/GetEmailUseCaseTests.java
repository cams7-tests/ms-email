package br.cams7.tests.ms.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.out.GetEmailRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailEntityTestData;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class GetEmailUseCaseTests {

  private static final EmailEntity DEFAULT_EMAIL_ENTITY = mock(EmailEntity.class);
  private static final ArgumentCaptor<UUID> EMAIL_ID_CAPTOR = ArgumentCaptor.forClass(UUID.class);

  private final GetEmailRepository getEmailRepository = mock(GetEmailRepository.class);
  private final GetEmailUseCase getEmailUseCase = new GetEmailUseCaseImpl(getEmailRepository);

  @BeforeEach
  void setUp() {
    given(getEmailRepository.findById(EMAIL_ID_CAPTOR.capture()))
        .willReturn(Optional.of(DEFAULT_EMAIL_ENTITY));
  }

  @Test
  @DisplayName("findById returns an email when successfull")
  void findById_ReturnsAnEmail_WhenSuccessful() {
    var email = getEmailUseCase.findById(EmailEntityTestData.EMAIL_ID);

    assertThat(email).isNotNull();
    assertThat(email.get()).isEqualTo(DEFAULT_EMAIL_ENTITY);
    assertThat(EMAIL_ID_CAPTOR.getValue()).isEqualTo(EmailEntityTestData.EMAIL_ID);

    then(getEmailRepository).should(times(1)).findById(eq(EMAIL_ID_CAPTOR.getValue()));
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
    willThrow(new RuntimeException()).given(getEmailRepository).findById(EMAIL_ID_CAPTOR.capture());

    assertThrows(
        RuntimeException.class,
        () -> {
          getEmailUseCase.findById(EmailEntityTestData.EMAIL_ID);
        });
    then(getEmailRepository).should(times(1)).findById(eq(EMAIL_ID_CAPTOR.getValue()));
  }
}
