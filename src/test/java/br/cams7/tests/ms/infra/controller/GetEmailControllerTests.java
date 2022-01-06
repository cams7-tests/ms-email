/** */
package br.cams7.tests.ms.infra.controller;

import static br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTOTestData.defaultEmailResponseDTO;
import static br.cams7.tests.ms.core.port.pagination.OrderDTOTestData.defaultOrderDTO;
import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.PAGE_NUMBER;
import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.PAGE_SIZE;
import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.defaultPageDTO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static reactor.test.StepVerifier.create;

import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.in.GetEmailsUseCase;
import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;
import br.cams7.tests.ms.core.port.pagination.OrderDTO;
import br.cams7.tests.ms.core.port.pagination.PageDTO;
import java.util.List;
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
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class GetEmailControllerTests {

  private static final String DEFAULT_SORT_FIELD = "emailSentDate";
  private static final Sort DEFAULT_SORT = Sort.by(DEFAULT_SORT_FIELD).ascending();
  private static final Pageable DEFAULT_PAGE = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, DEFAULT_SORT);
  private static final EmailResponseDTO DEFAULT_EMAIL_RESPONSE_DTO = defaultEmailResponseDTO();
  private static final PageDTO<EmailResponseDTO> PAGE_DTO_OF_RESPONSE_DTO =
      defaultPageDTO(DEFAULT_EMAIL_RESPONSE_DTO);
  public static final OrderDTO DEFAULT_ORDER_DTO = defaultOrderDTO();
  private static final String ERROR_MESSAGE = "Email not found.";

  @InjectMocks private GetEmailController getEmailController;

  @Spy private ModelMapper modelMapper = new ModelMapper();
  @Mock private GetEmailsUseCase getAllEmailsUseCase;
  @Mock private GetEmailUseCase getEmailUseCase;

  @Captor private ArgumentCaptor<List<OrderDTO>> orderDTOCaptor;

  @Test
  @DisplayName("getEmails returns emails when successfull")
  void getEmails_ReturnsEmails_WhenSuccessful() {
    given(getAllEmailsUseCase.findAll(anyInt(), anyInt(), anyList()))
        .willReturn(Mono.just(PAGE_DTO_OF_RESPONSE_DTO));

    create(getEmailController.getEmails(DEFAULT_PAGE))
        .expectSubscription()
        .expectNext(PAGE_DTO_OF_RESPONSE_DTO)
        .verifyComplete();

    then(getAllEmailsUseCase)
        .should()
        .findAll(eq(PAGE_NUMBER), eq(PAGE_SIZE), orderDTOCaptor.capture());
    assertThat(orderDTOCaptor.getValue()).isEqualTo(List.of(DEFAULT_ORDER_DTO));
  }

  @Test
  @DisplayName("getEmail returns an email when successfull")
  void getEmail_ReturnsAnEmail_WhenSuccessful() {

    given(getEmailUseCase.findById(any(UUID.class)))
        .willReturn(Mono.just(DEFAULT_EMAIL_RESPONSE_DTO));

    create(getEmailController.getEmail(EMAIL_ID))
        .expectSubscription()
        .expectNext(DEFAULT_EMAIL_RESPONSE_DTO)
        .verifyComplete();

    then(getEmailUseCase).should().findById(eq(EMAIL_ID));
  }

  @Test
  @DisplayName("getEmail returns an error message when get empty email")
  void getEmail_ReturnsAnErrorMessage_WhenGetEmptyEmail() {

    given(getEmailUseCase.findById(any(UUID.class)))
        .willReturn(Mono.error(new RuntimeException(ERROR_MESSAGE)));

    create(getEmailController.getEmail(EMAIL_ID))
        .expectSubscription()
        .expectError(RuntimeException.class)
        .verify();

    then(getEmailUseCase).should().findById(eq(EMAIL_ID));
  }
}
