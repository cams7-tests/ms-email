package br.cams7.tests.ms.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import br.cams7.tests.ms.core.common.PageDTO;
import br.cams7.tests.ms.core.common.PageDTOTestData;
import br.cams7.tests.ms.core.port.in.GetAllEmailsUseCase;
import br.cams7.tests.ms.core.port.out.EmailRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailEntityTestData;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GetAllEmailsUseCaseTests {

  private static final EmailEntity DEFAULT_EMAIL = EmailEntityTestData.defaultEmail();
  private static final PageDTO DEFAULT_PAGE = PageDTOTestData.defaultPage();

  private final EmailRepository emailRepository = Mockito.mock(EmailRepository.class);
  private final GetAllEmailsUseCase getAllEmailsUseCase =
      new GetAllEmailsUseCaseImpl(emailRepository);

  @BeforeEach
  void setUp() {
    given(emailRepository.findAll(any(PageDTO.class))).willReturn(Arrays.asList(DEFAULT_EMAIL));
  }

  @Test
  @DisplayName("findAll returns paged emails when successfull")
  void findAll_ReturnsPagedEmails_WhenSuccessful() {
    var emails = getAllEmailsUseCase.findAll(DEFAULT_PAGE);

    assertThat(emails).isNotEmpty();
    assertThat(emails.size()).isEqualTo(1);
    assertThat(emails).contains(DEFAULT_EMAIL);
  }
}
