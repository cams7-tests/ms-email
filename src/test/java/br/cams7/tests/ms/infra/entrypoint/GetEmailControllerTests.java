/** */
package br.cams7.tests.ms.infra.entrypoint;

import static br.cams7.tests.ms.core.port.pagination.OrderDTOTestData.getOrderDTO;
import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.PAGE_NUMBER;
import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.PAGE_SIZE;
import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.getPageDTO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_ID;
import static br.cams7.tests.ms.domain.EmailEntityTestData.getEmailEntity;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.getEmailResponseDTO;
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
import br.cams7.tests.ms.core.port.pagination.OrderDTO;
import br.cams7.tests.ms.core.port.pagination.PageDTO;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.infra.entrypoint.mapper.ResponseConverter;
import br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTO;
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
  private static final EmailEntity DEFAULT_EMAIL_ENTITY = getEmailEntity();
  private static final EmailResponseDTO DEFAULT_EMAIL_RESPONSE_DTO = getEmailResponseDTO();
  private static final PageDTO<EmailEntity> PAGE_DTO_OF_ENTITY = getPageDTO(DEFAULT_EMAIL_ENTITY);
  private static final PageDTO<EmailResponseDTO> PAGE_DTO_OF_RESPONSE_DTO =
      getPageDTO(DEFAULT_EMAIL_RESPONSE_DTO);
  public static final OrderDTO DEFAULT_ORDER_DTO = getOrderDTO();
  private static final String ERROR_MESSAGE = "Email not found.";

  @InjectMocks private GetEmailController getEmailController;

  @Spy private ModelMapper modelMapper = new ModelMapper();
  @Mock private GetEmailsUseCase getAllEmailsUseCase;
  @Mock private GetEmailUseCase getEmailUseCase;
  @Mock private ResponseConverter responseConverter;

  @Captor private ArgumentCaptor<List<OrderDTO>> orderDTOCaptor;

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("getEmails returns emails when successfull")
  void getEmails_ReturnsEmails_WhenSuccessful() {
    given(getAllEmailsUseCase.execute(anyInt(), anyInt(), anyList()))
        .willReturn(Mono.just(PAGE_DTO_OF_ENTITY));
    given(responseConverter.convert(any(PageDTO.class))).willReturn(PAGE_DTO_OF_RESPONSE_DTO);

    create(getEmailController.getEmails(DEFAULT_PAGE))
        .expectSubscription()
        .expectNext(PAGE_DTO_OF_RESPONSE_DTO)
        .verifyComplete();

    then(getAllEmailsUseCase)
        .should()
        .execute(eq(PAGE_NUMBER), eq(PAGE_SIZE), orderDTOCaptor.capture());
    assertThat(orderDTOCaptor.getValue()).isEqualTo(List.of(DEFAULT_ORDER_DTO));
    then(responseConverter).should().convert(eq(PAGE_DTO_OF_ENTITY));
  }

  @Test
  @DisplayName("getEmail returns an email when successfull")
  void getEmail_ReturnsAnEmail_WhenSuccessful() {

    given(getEmailUseCase.execute(any(UUID.class))).willReturn(Mono.just(DEFAULT_EMAIL_ENTITY));
    given(responseConverter.convert(any(EmailEntity.class))).willReturn(DEFAULT_EMAIL_RESPONSE_DTO);

    create(getEmailController.getEmail(EMAIL_ID))
        .expectSubscription()
        .expectNext(DEFAULT_EMAIL_RESPONSE_DTO)
        .verifyComplete();

    then(getEmailUseCase).should().execute(eq(EMAIL_ID));
    then(responseConverter).should().convert(eq(DEFAULT_EMAIL_ENTITY));
  }

  @Test
  @DisplayName("getEmail returns an error message when get empty email")
  void getEmail_ReturnsAnErrorMessage_WhenGetEmptyEmail() {

    given(getEmailUseCase.execute(any(UUID.class)))
        .willReturn(Mono.error(new RuntimeException(ERROR_MESSAGE)));

    create(getEmailController.getEmail(EMAIL_ID))
        .expectSubscription()
        .expectError(RuntimeException.class)
        .verify();

    then(getEmailUseCase).should().execute(eq(EMAIL_ID));
  }
}
