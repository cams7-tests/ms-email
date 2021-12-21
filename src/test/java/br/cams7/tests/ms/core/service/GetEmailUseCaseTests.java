package br.cams7.tests.ms.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.out.EmailRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailEntityTestData;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GetEmailUseCaseTests {

  private static final EmailEntity DEFAULT_EMAIL = EmailEntityTestData.defaultEmail();

  private final EmailRepository emailRepository = Mockito.mock(EmailRepository.class);
  private final GetEmailUseCase getEmailUseCase = new GetEmailUseCaseImpl(emailRepository);

  @BeforeEach
  void setUp() {
    given(emailRepository.findById(any(UUID.class))).willReturn(Optional.of(DEFAULT_EMAIL));
  }

  @Test
  @DisplayName("findById returns an email when successfull")
  void findById_ReturnsAnEmail_WhenSuccessful() {
    var email = getEmailUseCase.findById(EmailEntityTestData.EMAIL_ID);

    assertThat(email).isNotNull();
    assertThat(email.get()).isEqualTo(DEFAULT_EMAIL);
  }
}
