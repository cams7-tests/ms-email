package br.cams7.tests.ms.infra.webclient;

import static br.cams7.tests.ms.domain.EmailEntityTestData.OWNER_REF;
import static br.cams7.tests.ms.infra.webclient.response.CheckIdentificationNumberResponseTestData.aprovedCheckIdentificationNumberResponse;
import static br.cams7.tests.ms.infra.webclient.response.CheckIdentificationNumberResponseTestData.notAprovedCheckIdentificationNumberResponse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static reactor.test.StepVerifier.create;

import br.cams7.tests.ms.infra.webclient.response.CheckIdentificationNumberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class CheckIdentificationNumberServiceTests {

  private static final CheckIdentificationNumberResponse
      APROVED_CHECK_IDENTIFICATION_NUMBER_RESPONSE = aprovedCheckIdentificationNumberResponse();
  private static final CheckIdentificationNumberResponse
      NOT_APROVED_CHECK_IDENTIFICATION_NUMBER_RESPONSE =
          notAprovedCheckIdentificationNumberResponse();
  private static final String IDENTIFICATION_NUMBER = OWNER_REF;
  private static final String ERROR_MESSAGE = "Error";

  @InjectMocks private CheckIdentificationNumberService checkIdentificationNumberService;

  @Mock private WebClient checkIdentificationNumber;

  @Test
  @DisplayName("isValid when aproved")
  void isValid_WhenAproved() {
    mockCheckIdentificationNumber(
        IDENTIFICATION_NUMBER, APROVED_CHECK_IDENTIFICATION_NUMBER_RESPONSE);

    create(checkIdentificationNumberService.isValid(IDENTIFICATION_NUMBER))
        .expectSubscription()
        .expectNext(true)
        .verifyComplete();
  }

  @Test
  @DisplayName("isValid when not aproved")
  void isValid_WhenNotAproved() {

    mockCheckIdentificationNumber(
        IDENTIFICATION_NUMBER, NOT_APROVED_CHECK_IDENTIFICATION_NUMBER_RESPONSE);

    create(checkIdentificationNumberService.isValid(IDENTIFICATION_NUMBER))
        .expectSubscription()
        .expectNext(false)
        .verifyComplete();
  }

  @Test
  @DisplayName("isValid throws error when pass null identification number")
  void isValid_ThrowsError_WhenPassNullIdentificationNumber() {
    assertThrows(
        NullPointerException.class,
        () -> {
          checkIdentificationNumberService.isValid(null);
        });
  }

  @Test
  @DisplayName(
      "isValid returns error when some error happened during validation of identification number")
  void isValid_ReturnsError_WhenSomeErrorHappenedDuringValidationOfIdenficationNumber() {

    mockCheckIdentificationNumber(
        IDENTIFICATION_NUMBER, Mono.error(new RuntimeException(ERROR_MESSAGE)));

    create(checkIdentificationNumberService.isValid(IDENTIFICATION_NUMBER))
        .expectSubscription()
        .expectError(RuntimeException.class)
        .verify();
  }

  private void mockCheckIdentificationNumber(
      String identificationNumber, CheckIdentificationNumberResponse response) {
    mockCheckIdentificationNumber(identificationNumber, Mono.just(response));
  }

  @SuppressWarnings("unchecked")
  private void mockCheckIdentificationNumber(
      String identificationNumber, Mono<CheckIdentificationNumberResponse> response) {
    var requestHeadersMock = mock(WebClient.RequestHeadersSpec.class);
    var requestHeadersUriMock = mock(WebClient.RequestHeadersUriSpec.class);
    // var requestBodyMock = mock(WebClient.RequestBodySpec.class) ;
    // var requestBodyUriMock = mock(WebClient.RequestBodyUriSpec.class) ;
    var responseMock = mock(WebClient.ResponseSpec.class);

    given(checkIdentificationNumber.get()).willReturn(requestHeadersUriMock);
    given(requestHeadersUriMock.uri("/serasa/{cpf}", identificationNumber))
        .willReturn(requestHeadersMock);
    given(requestHeadersMock.retrieve()).willReturn(responseMock);
    given(responseMock.bodyToMono(CheckIdentificationNumberResponse.class)).willReturn(response);
  }
}
