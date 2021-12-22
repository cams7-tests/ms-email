package br.cams7.tests.ms.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.out.EmailRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailEntityTestData;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class GetEmailUseCaseTests {

  private static final EmailEntity DEFAULT_EMAIL_ENTITY = Mockito.mock(EmailEntity.class);
  private static final ArgumentCaptor<UUID> EMAIL_ID_CAPTOR = ArgumentCaptor.forClass(UUID.class);

  private final EmailRepository emailRepository = Mockito.mock(EmailRepository.class);
  private final GetEmailUseCase getEmailUseCase = new GetEmailUseCaseImpl(emailRepository);

  @BeforeEach
  void setUp() {
    given(emailRepository.findById(EMAIL_ID_CAPTOR.capture()))
        .willReturn(Optional.of(DEFAULT_EMAIL_ENTITY));
  }

  @Test
  @DisplayName("findById returns an email when successfull")
  void findById_ReturnsAnEmail_WhenSuccessful() {
    var email = getEmailUseCase.findById(EmailEntityTestData.EMAIL_ID);

    assertThat(email).isNotNull();
    assertThat(email.get()).isEqualTo(DEFAULT_EMAIL_ENTITY);
    assertThat(EMAIL_ID_CAPTOR.getValue()).isEqualTo(EmailEntityTestData.EMAIL_ID);
    verify(emailRepository, times(1)).findById(EMAIL_ID_CAPTOR.getValue());
  }

  @Test
  @DisplayName("findById throws error when pass null email id")
  void findById_ThrowsError_WhenPassNullEmailId() {
    doThrow(new NullPointerException()).when(emailRepository).findById(null);

    assertThrows(
        NullPointerException.class,
        () -> {
          getEmailUseCase.findById(null);
        });
    verify(emailRepository, times(1)).findById(null);
  }

  @Test
  @DisplayName("findById throws error when empty don't find email")
  void findById_ThrowsError_WhenDontFindEmail() {
    doThrow(new RuntimeException()).when(emailRepository).findById(EMAIL_ID_CAPTOR.capture());

    assertThrows(
        RuntimeException.class,
        () -> {
          getEmailUseCase.findById(EmailEntityTestData.EMAIL_ID);
        });
    verify(emailRepository, times(1)).findById(EMAIL_ID_CAPTOR.getValue());
  }
}
