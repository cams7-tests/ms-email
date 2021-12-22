package br.cams7.tests.ms.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import br.cams7.tests.ms.core.common.PageDTO;
import br.cams7.tests.ms.core.port.in.GetAllEmailsUseCase;
import br.cams7.tests.ms.core.port.out.EmailRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class GetAllEmailsUseCaseTests {

  private static final EmailEntity DEFAULT_EMAIL_ENTITY = Mockito.mock(EmailEntity.class);
  private static final PageDTO DEFAULT_PAGE_DTO = Mockito.mock(PageDTO.class);
  private static final ArgumentCaptor<PageDTO> PAGE_DTO_CAPTOR =
      ArgumentCaptor.forClass(PageDTO.class);

  private final EmailRepository emailRepository = Mockito.mock(EmailRepository.class);
  private final GetAllEmailsUseCase getAllEmailsUseCase =
      new GetAllEmailsUseCaseImpl(emailRepository);

  @BeforeEach
  void setUp() {
    given(emailRepository.findAll(PAGE_DTO_CAPTOR.capture()))
        .willReturn(Arrays.asList(DEFAULT_EMAIL_ENTITY));
  }

  @Test
  @DisplayName("findAll returns paged emails when successfull")
  void findAll_ReturnsPagedEmails_WhenSuccessful() {
    var emails = getAllEmailsUseCase.findAll(DEFAULT_PAGE_DTO);

    assertThat(emails).isNotEmpty();
    assertThat(emails.size()).isEqualTo(1);
    assertThat(emails).contains(DEFAULT_EMAIL_ENTITY);
    assertThat(PAGE_DTO_CAPTOR.getValue()).isEqualTo(DEFAULT_PAGE_DTO);

    then(emailRepository).should(times(1)).findAll(eq(PAGE_DTO_CAPTOR.getValue()));
  }
}
