package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.common.PageDTOTestData.defaultPageDTO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.defaultEmailEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import br.cams7.tests.ms.core.common.PageDTO;
import br.cams7.tests.ms.core.port.out.GetEmailsRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetEmailsUseCaseTests {

  private static final EmailEntity DEFAULT_EMAIL_ENTITY = defaultEmailEntity();
  private static final PageDTO DEFAULT_PAGE_DTO = defaultPageDTO();
  private static final int TOTAL_EMAILS = 1;

  @InjectMocks private GetEmailsUseCaseImpl getAllEmailsUseCase;

  @Mock private GetEmailsRepository getEmailsRepository;

  @Test
  @DisplayName("findAll returns paged emails when successfull")
  void findAll_ReturnsPagedEmails_WhenSuccessful() {
    given(getEmailsRepository.findAll(any(PageDTO.class)))
        .willReturn(Arrays.asList(DEFAULT_EMAIL_ENTITY));

    var emails = getAllEmailsUseCase.findAll(DEFAULT_PAGE_DTO);

    assertThat(emails).hasSize(TOTAL_EMAILS);
    assertThat(emails).contains(DEFAULT_EMAIL_ENTITY);

    then(getEmailsRepository).should(times(1)).findAll(eq(DEFAULT_PAGE_DTO));
  }
}
