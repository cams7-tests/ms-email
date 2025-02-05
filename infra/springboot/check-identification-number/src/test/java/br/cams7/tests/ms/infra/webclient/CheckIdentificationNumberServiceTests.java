package br.cams7.tests.ms.infra.webclient;

import static br.cams7.tests.ms.domain.EmailEntityTestData.OWNER_REF;
import static br.cams7.tests.ms.infra.webclient.response.CheckIdentificationNumberResponseTestData.getAprovedCheckIdentificationNumberResponse;
import static br.cams7.tests.ms.infra.webclient.response.CheckIdentificationNumberResponseTestData.getNotAprovedCheckIdentificationNumberResponse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static reactor.test.StepVerifier.create;

import br.cams7.tests.ms.infra.webclient.response.CheckIdentificationNumberResponse;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@ExtendWith(MockitoExtension.class)
class CheckIdentificationNumberServiceTests {

  private static final CheckIdentificationNumberResponse
      APROVED_CHECK_IDENTIFICATION_NUMBER_RESPONSE = getAprovedCheckIdentificationNumberResponse();
  private static final CheckIdentificationNumberResponse
      NOT_APROVED_CHECK_IDENTIFICATION_NUMBER_RESPONSE =
          getNotAprovedCheckIdentificationNumberResponse();
  private static final String IDENTIFICATION_NUMBER = OWNER_REF;
  private static final String ERROR_MESSAGE = "Error";

  @InjectMocks private CheckIdentificationNumberService checkIdentificationNumberService;

  @Mock private WebClient checkIdentificationNumberWebClient;

  @BeforeAll
  static void blockHoundSetup() {
    BlockHound.install();
  }

  @Test
  void blockHoundWorks() {
    try {
      FutureTask<?> task =
          new FutureTask<>(
              () -> {
                Thread.sleep(0); // NOSONAR
                return "";
              });
      Schedulers.parallel().schedule(task);

      task.get(10, TimeUnit.SECONDS);
      fail("should fail");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof BlockingOperationError);
    }
  }

  @Test
  @DisplayName("isValid when aproved")
  void isValid_WhenAproved() {
    mockCheckIdentificationNumberWebClient(
        IDENTIFICATION_NUMBER, APROVED_CHECK_IDENTIFICATION_NUMBER_RESPONSE);

    create(checkIdentificationNumberService.isValid(IDENTIFICATION_NUMBER))
        .expectSubscription()
        .expectNext(true)
        .verifyComplete();
  }

  @Test
  @DisplayName("isValid when not aproved")
  void isValid_WhenNotAproved() {

    mockCheckIdentificationNumberWebClient(
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

    mockCheckIdentificationNumberWebClient(
        IDENTIFICATION_NUMBER, Mono.error(new RuntimeException(ERROR_MESSAGE)));

    create(checkIdentificationNumberService.isValid(IDENTIFICATION_NUMBER))
        .expectSubscription()
        .expectError(RuntimeException.class)
        .verify();
  }

  private void mockCheckIdentificationNumberWebClient(
      String identificationNumber, CheckIdentificationNumberResponse response) {
    mockCheckIdentificationNumberWebClient(identificationNumber, Mono.just(response));
  }

  @SuppressWarnings("unchecked")
  private void mockCheckIdentificationNumberWebClient(
      String identificationNumber, Mono<CheckIdentificationNumberResponse> response) {
    var requestHeadersMock = mock(WebClient.RequestHeadersSpec.class);
    var requestHeadersUriMock = mock(WebClient.RequestHeadersUriSpec.class);
    // var requestBodyMock = mock(WebClient.RequestBodySpec.class) ;
    // var requestBodyUriMock = mock(WebClient.RequestBodyUriSpec.class) ;
    var responseMock = mock(WebClient.ResponseSpec.class);

    given(checkIdentificationNumberWebClient.get()).willReturn(requestHeadersUriMock);
    given(requestHeadersUriMock.uri("/serasa/{cpf}", identificationNumber))
        .willReturn(requestHeadersMock);
    given(requestHeadersMock.retrieve()).willReturn(responseMock);
    given(responseMock.bodyToMono(CheckIdentificationNumberResponse.class)).willReturn(response);
  }
}
