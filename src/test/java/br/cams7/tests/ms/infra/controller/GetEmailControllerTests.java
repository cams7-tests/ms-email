/** */
package br.cams7.tests.ms.infra.controller;

import static br.cams7.tests.ms.core.common.PageDTOTestData.PAGE_NUMBER;
import static br.cams7.tests.ms.core.common.PageDTOTestData.PAGE_SIZE;
import static br.cams7.tests.ms.core.common.PageDTOTestData.defaultPageDTO;
import static br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTOTestData.defaultEmailResponseDTO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import br.cams7.tests.ms.core.common.PageDTO;
import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.in.GetEmailsUseCase;
import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class GetEmailControllerTests {

  private static final String DEFAULT_SORT_FIELD = "emailFrom";
  private static final Sort DEFAULT_SORT = Sort.by(DEFAULT_SORT_FIELD).ascending();
  private static final Pageable DEFAULT_PAGE = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, DEFAULT_SORT);
  private static final PageDTO DEFAULT_PAGE_DTO = defaultPageDTO();
  private static final EmailResponseDTO DEFAULT_EMAIL_RESPONSE_DTO = defaultEmailResponseDTO();
  private static final int TOTAL_RETURNED_EMAILS = 1;
  private static final String ERROR_MESSAGE = "Email not found.";

  @InjectMocks private GetEmailController getEmailController;

  @Spy private ModelMapper modelMapper = new ModelMapper();
  @Mock private GetEmailsUseCase getAllEmailsUseCase;
  @Mock private GetEmailUseCase getEmailUseCase;

  @Captor private ArgumentCaptor<PageDTO> pageDTOCaptor;

  @Test
  @DisplayName("getEmails returns emails when successfull")
  void getEmails_ReturnsEmails_WhenSuccessful() {
    final var emails = Arrays.asList(DEFAULT_EMAIL_RESPONSE_DTO);

    given(getAllEmailsUseCase.findAll(any(PageDTO.class))).willReturn(emails);

    var response = getEmailController.getEmails(DEFAULT_PAGE);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.hasBody()).isTrue();

    var page = response.getBody();
    assertThat(page).isNotNull();
    assertThat(page.getPageable()).isEqualTo(DEFAULT_PAGE);
    assertThat(page.getNumber()).isEqualTo(PAGE_NUMBER);
    assertThat(page.getSize()).isEqualTo(PAGE_SIZE);
    assertThat(page.getSort()).isEqualTo(DEFAULT_SORT);
    assertThat(page.getNumberOfElements()).isEqualTo(TOTAL_RETURNED_EMAILS);
    assertThat(page.getTotalElements()).isEqualTo(TOTAL_RETURNED_EMAILS);
    assertThat(page.getTotalPages()).isEqualTo(TOTAL_RETURNED_EMAILS);

    var content = page.getContent();
    assertThat(content).isEqualTo(emails);

    then(getAllEmailsUseCase).should().findAll(pageDTOCaptor.capture());
    assertThat(pageDTOCaptor.getValue()).isEqualTo(DEFAULT_PAGE_DTO);
  }

  @Test
  @DisplayName("getEmail returns an email when successfull")
  void getEmail_ReturnsAnEmail_WhenSuccessful() {

    given(getEmailUseCase.findById(any(UUID.class)))
        .willReturn(Optional.of(DEFAULT_EMAIL_RESPONSE_DTO));

    var response = getEmailController.getEmail(EMAIL_ID);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.hasBody()).isTrue();

    var email = response.getBody();
    assertThat(email).isEqualTo(DEFAULT_EMAIL_RESPONSE_DTO);

    then(getEmailUseCase).should().findById(eq(EMAIL_ID));
  }

  @Test
  @DisplayName("getEmail returns an error message when get empty email")
  void getEmail_ReturnsAnErrorMessage_WhenGetEmptyEmail() {

    given(getEmailUseCase.findById(any(UUID.class))).willReturn(Optional.empty());

    var response = getEmailController.getEmail(EMAIL_ID);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.hasBody()).isTrue();

    var message = response.getBody();
    assertThat(message).isEqualTo(ERROR_MESSAGE);

    then(getEmailUseCase).should().findById(eq(EMAIL_ID));
  }
}
