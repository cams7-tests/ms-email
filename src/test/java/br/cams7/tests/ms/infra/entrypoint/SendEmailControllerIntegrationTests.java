package br.cams7.tests.ms.infra.entrypoint;

import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_FROM;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_TO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_SUBJECT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_TEXT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.NEW_EMAIL_ENTITY;
import static br.cams7.tests.ms.domain.EmailEntityTestData.OWNER_REF;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.NEW_EMAIL_RESPONSE_DTO;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.NEW_EMAIL_WITH_EMPTY_SUBJECT;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.NEW_EMAIL_WITH_INVALID_EMAIL_FROM;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.NEW_EMAIL_WITH_INVALID_IDENTIFICATION_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberGateway;
import br.cams7.tests.ms.core.port.out.SendEmailGateway;
import br.cams7.tests.ms.core.port.out.SendEmailToQueueGateway;
import br.cams7.tests.ms.core.port.out.exception.SendEmailException;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailStatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = {
      "spring.r2dbc.url=r2dbc:h2:mem:///send-email-controller-tests?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
    })
@AutoConfigureWebTestClient
class SendEmailControllerIntegrationTests {

  private static final boolean IS_VALID_IDENTIFICATION_NUMBER = true;
  private static final boolean IS_INVALID_IDENTIFICATION_NUMBER = false;

  private static final String ERROR_MESSAGE = "Error";

  @Autowired private WebTestClient testClient;

  @MockBean private CheckIdentificationNumberGateway checkIdentificationNumberGateway;
  @MockBean private SendEmailGateway sendEmailGateway;
  @MockBean private SendEmailToQueueGateway sendEmailToQueueGateway;

  @Captor private ArgumentCaptor<EmailEntity> emailEntityCaptor;

  @Test
  @DisplayName("Sent an email directly when pass valid params")
  void sendEmailDirectly_SendAnEmailDirectly_WhenPassValidParams() throws SendEmailException {
    given(checkIdentificationNumberGateway.isValid(anyString()))
        .willReturn(Mono.just(IS_VALID_IDENTIFICATION_NUMBER));
    willDoNothing().given(sendEmailGateway).sendEmail(any(EmailEntity.class));

    testClient
        .post()
        .uri("/send-email-directly")
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(NEW_EMAIL_RESPONSE_DTO))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.identificationNumber")
        .isEqualTo(OWNER_REF)
        .jsonPath("$.emailFrom")
        .isEqualTo(EMAIL_FROM)
        .jsonPath("$.emailTo")
        .isEqualTo(EMAIL_TO)
        .jsonPath("$.subject")
        .isEqualTo(MESSAGE_SUBJECT)
        .jsonPath("$.text")
        .isEqualTo(MESSAGE_TEXT)
        .jsonPath("$.emailStatus")
        .isEqualTo(EmailStatusEnum.SENT.name())
        .jsonPath("$.emailSentDate")
        .isNotEmpty()
        .jsonPath("$.emailId")
        .isNotEmpty();

    then(checkIdentificationNumberGateway).should(times(1)).isValid(eq(OWNER_REF));
    then(sendEmailGateway).should(times(1)).sendEmail(emailEntityCaptor.capture());
    assertThat(emailEntityCaptor.getValue().withEmailId(null).withEmailSentDate(null))
        .isEqualTo(NEW_EMAIL_ENTITY);
  }

  @Test
  @DisplayName("Returns error when pass empty subject")
  void sendEmailDirectly_ReturnsError_WhenPassEmptySubject() {
    testClient
        .post()
        .uri("/send-email-directly")
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(NEW_EMAIL_WITH_EMPTY_SUBJECT))
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody();
  }

  @Test
  @DisplayName("Returns error when pass 'identification number' that isn't a valid CPF")
  void sendEmailDirectly_ReturnsError_WhenPassIdentificationNumberThatIsntAValidCpf() {
    testClient
        .post()
        .uri("/send-email-directly")
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(NEW_EMAIL_WITH_INVALID_IDENTIFICATION_NUMBER))
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody();
  }

  @Test
  @DisplayName("Returns error when pass 'email from' that isn't a valid email")
  void sendEmailDirectly_ReturnsError_WhenPassEmailFromThatIsntAValidEmail() {
    testClient
        .post()
        .uri("/send-email-directly")
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(NEW_EMAIL_WITH_INVALID_EMAIL_FROM))
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody();
  }

  @Test
  @DisplayName("Returns error when pass a invalid 'identification number'")
  void sendEmailDirectly_ReturnsError_WhenPassAInvalidIdentificationNumber()
      throws SendEmailException {
    given(checkIdentificationNumberGateway.isValid(anyString()))
        .willReturn(Mono.just(IS_INVALID_IDENTIFICATION_NUMBER));
    testClient
        .post()
        .uri("/send-email-directly")
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(NEW_EMAIL_RESPONSE_DTO))
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody();

    then(checkIdentificationNumberGateway).should(times(1)).isValid(eq(OWNER_REF));
    then(sendEmailGateway).should(never()).sendEmail(any(EmailEntity.class));
  }

  @Test
  @DisplayName("Returns error when some error happened during 'identification number' validation")
  void sendEmailDirectly_ReturnsError_WhenSomeErrorHappenedDuringIdentificationNumberValidation()
      throws SendEmailException {
    given(checkIdentificationNumberGateway.isValid(anyString()))
        .willReturn(Mono.error(new RuntimeException(ERROR_MESSAGE)));
    testClient
        .post()
        .uri("/send-email-directly")
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(NEW_EMAIL_RESPONSE_DTO))
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody();

    then(checkIdentificationNumberGateway).should(times(1)).isValid(eq(OWNER_REF));
    then(sendEmailGateway).should(never()).sendEmail(any(EmailEntity.class));
  }

  @Test
  @DisplayName("Save an email with error status when some error happened during try sent email")
  void sendEmailDirectly_SaveAnEmailWithErrorStatus_WhenSomeErrorHappenedDuringTrySendEmail()
      throws SendEmailException {
    given(checkIdentificationNumberGateway.isValid(anyString()))
        .willReturn(Mono.just(IS_VALID_IDENTIFICATION_NUMBER));
    willThrow(new SendEmailException(ERROR_MESSAGE, null))
        .given(sendEmailGateway)
        .sendEmail(any(EmailEntity.class));

    testClient
        .post()
        .uri("/send-email-directly")
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(NEW_EMAIL_RESPONSE_DTO))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.emailStatus")
        .isEqualTo(EmailStatusEnum.ERROR.name());

    then(checkIdentificationNumberGateway).should(times(1)).isValid(eq(OWNER_REF));
    then(sendEmailGateway).should(times(1)).sendEmail(emailEntityCaptor.capture());
    assertThat(emailEntityCaptor.getValue().withEmailId(null).withEmailSentDate(null))
        .isEqualTo(NEW_EMAIL_ENTITY.withEmailStatus(EmailStatusEnum.ERROR));
  }

  @Test
  @DisplayName("Sent an email indirectly when pass valid params")
  void sendEmailToQueue_SendAnEmailIndirectly_WhenPassValidParams() throws SendEmailException {
    given(checkIdentificationNumberGateway.isValid(anyString()))
        .willReturn(Mono.just(IS_VALID_IDENTIFICATION_NUMBER));
    willDoNothing().given(sendEmailToQueueGateway).sendEmail(any(EmailEntity.class));

    testClient
        .post()
        .uri("/send-email-to-queue")
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(NEW_EMAIL_RESPONSE_DTO))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .isEmpty();

    then(checkIdentificationNumberGateway).should(times(1)).isValid(eq(OWNER_REF));
    then(sendEmailToQueueGateway).should(times(1)).sendEmail(emailEntityCaptor.capture());
    assertThat(emailEntityCaptor.getValue()).isEqualTo(NEW_EMAIL_ENTITY.withEmailStatus(null));
  }

  @Test
  @DisplayName("Returns error when some error happened during try sent email to queue")
  void sendEmailToQueue_ReturnsError_WhenSomeErrorHappenedDuringTrySentEmailToQueue()
      throws SendEmailException {
    given(checkIdentificationNumberGateway.isValid(anyString()))
        .willReturn(Mono.just(IS_VALID_IDENTIFICATION_NUMBER));
    willThrow(new RuntimeException(ERROR_MESSAGE))
        .given(sendEmailToQueueGateway)
        .sendEmail(any(EmailEntity.class));

    testClient
        .post()
        .uri("/send-email-to-queue")
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(NEW_EMAIL_RESPONSE_DTO))
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody();

    then(checkIdentificationNumberGateway).should(times(1)).isValid(eq(OWNER_REF));
    then(sendEmailToQueueGateway).should(times(1)).sendEmail(emailEntityCaptor.capture());
    assertThat(emailEntityCaptor.getValue()).isEqualTo(NEW_EMAIL_ENTITY.withEmailStatus(null));
  }
}
