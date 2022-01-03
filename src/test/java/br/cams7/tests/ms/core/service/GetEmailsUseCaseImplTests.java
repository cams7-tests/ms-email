package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.common.PageDTOTestData.defaultPageDTO;
import static br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTOTestData.defaultEmailResponseDTO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.defaultEmailEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;

import br.cams7.tests.ms.core.common.PageDTO;
import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;
import br.cams7.tests.ms.core.port.out.GetEmailsRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class GetEmailsUseCaseImplTests {

  private static final EmailEntity DEFAULT_EMAIL_ENTITY = defaultEmailEntity();
  private static final EmailResponseDTO DEFAULT_EMAIL_RESPONSE_DTO = defaultEmailResponseDTO();
  private static final PageDTO DEFAULT_PAGE_DTO = defaultPageDTO();

  @InjectMocks private GetEmailsUseCaseImpl getAllEmailsUseCase;

  @Spy private ModelMapper modelMapper = new ModelMapper();
  @Mock private GetEmailsRepository getEmailsRepository;

  @Test
  @DisplayName("findAll returns paged emails when successfull")
  void findAll_ReturnsPagedEmails_WhenSuccessful() {
    given(getEmailsRepository.findAll(any(PageDTO.class)))
        .willReturn(Arrays.asList(DEFAULT_EMAIL_ENTITY));

    var emails = getAllEmailsUseCase.findAll(DEFAULT_PAGE_DTO);

    assertThat(emails).contains(DEFAULT_EMAIL_RESPONSE_DTO);

    then(getEmailsRepository).should(times(1)).findAll(eq(DEFAULT_PAGE_DTO));
  }

  @Test
  @DisplayName("findAll throws error when some error happened during get emails")
  void findAll_ThrowsError_WhenSomeErrorHappenedDuringGetEmails() {
    willThrow(RuntimeException.class).given(getEmailsRepository).findAll(any(PageDTO.class));

    assertThrows(
        RuntimeException.class,
        () -> {
          getAllEmailsUseCase.findAll(DEFAULT_PAGE_DTO);
        });

    then(getEmailsRepository).should(times(1)).findAll(eq(DEFAULT_PAGE_DTO));
  }
}
