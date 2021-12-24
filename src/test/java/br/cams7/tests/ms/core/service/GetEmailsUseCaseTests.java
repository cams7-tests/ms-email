package br.cams7.tests.ms.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import br.cams7.tests.ms.core.common.PageDTO;
import br.cams7.tests.ms.core.common.PageDTOTestData;
import br.cams7.tests.ms.core.port.in.GetEmailsUseCase;
import br.cams7.tests.ms.core.port.out.GetEmailsRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailEntityTestData;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class GetEmailsUseCaseTests {

  private static final EmailEntity DEFAULT_EMAIL_ENTITY = EmailEntityTestData.defaultEmail();
  private static final PageDTO DEFAULT_PAGE_DTO = PageDTOTestData.defaultPage();
  private static final ArgumentCaptor<PageDTO> PAGE_DTO_CAPTOR =
      ArgumentCaptor.forClass(PageDTO.class);
  private static final int TOTAL_EMAILS = 1;

  private final GetEmailsRepository getEmailsRepository = mock(GetEmailsRepository.class);
  private final GetEmailsUseCase getAllEmailsUseCase =
      new GetEmailsUseCaseImpl(getEmailsRepository);

  @BeforeEach
  void setUp() {
    given(getEmailsRepository.findAll(any(PageDTO.class)))
        .willReturn(Arrays.asList(DEFAULT_EMAIL_ENTITY));
  }

  @Test
  @DisplayName("findAll returns paged emails when successfull")
  void findAll_ReturnsPagedEmails_WhenSuccessful() {
    var emails = getAllEmailsUseCase.findAll(DEFAULT_PAGE_DTO);

    assertThat(emails).hasSize(TOTAL_EMAILS);
    assertThat(emails).contains(DEFAULT_EMAIL_ENTITY);

    then(getEmailsRepository).should(times(1)).findAll(PAGE_DTO_CAPTOR.capture());

    assertThat(PAGE_DTO_CAPTOR.getValue()).isEqualTo(DEFAULT_PAGE_DTO);
  }
}
