package br.cams7.tests.ms.infra.entrypoint;

import static br.cams7.tests.ms.infra.entrypoint.EmailResponseCreator.NEW_EMAIL;
import static br.cams7.tests.ms.infra.entrypoint.EmailResponseCreator.NEW_EMAIL_ENTITY;
import static br.cams7.tests.ms.infra.entrypoint.EmailResponseCreator.NEW_EMAIL_FROM;
import static br.cams7.tests.ms.infra.entrypoint.EmailResponseCreator.NEW_EMAIL_IDENTIFICATION_NUMBER;
import static br.cams7.tests.ms.infra.entrypoint.EmailResponseCreator.NEW_EMAIL_SUBJECT;
import static br.cams7.tests.ms.infra.entrypoint.EmailResponseCreator.NEW_EMAIL_TEXT;
import static br.cams7.tests.ms.infra.entrypoint.EmailResponseCreator.NEW_EMAIL_TO;
import static br.cams7.tests.ms.infra.entrypoint.EmailResponseCreator.NEW_EMAIL_WITH_EMPTY_SUBJECT;
import static br.cams7.tests.ms.infra.entrypoint.EmailResponseCreator.NEW_EMAIL_WITH_INVALID_EMAIL_FROM;
import static br.cams7.tests.ms.infra.entrypoint.EmailResponseCreator.NEW_EMAIL_WITH_INVALID_IDENTIFICATION_NUMBER;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
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
        .body(BodyInserters.fromValue(NEW_EMAIL))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.identificationNumber")
        .isEqualTo(NEW_EMAIL_IDENTIFICATION_NUMBER)
        .jsonPath("$.emailFrom")
        .isEqualTo(NEW_EMAIL_FROM)
        .jsonPath("$.emailTo")
        .isEqualTo(NEW_EMAIL_TO)
        .jsonPath("$.subject")
        .isEqualTo(NEW_EMAIL_SUBJECT)
        .jsonPath("$.text")
        .isEqualTo(NEW_EMAIL_TEXT)
        .jsonPath("$.emailStatus")
        .isEqualTo(EmailStatusEnum.SENT.name())
        .jsonPath("$.emailSentDate")
        .isNotEmpty()
        .jsonPath("$.emailId")
        .isNotEmpty();

    then(checkIdentificationNumberGateway)
        .should(times(1))
        .isValid(eq(NEW_EMAIL_IDENTIFICATION_NUMBER));
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
        .body(BodyInserters.fromValue(NEW_EMAIL))
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody();

    then(checkIdentificationNumberGateway)
        .should(times(1))
        .isValid(eq(NEW_EMAIL_IDENTIFICATION_NUMBER));
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
        .body(BodyInserters.fromValue(NEW_EMAIL))
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody();

    then(checkIdentificationNumberGateway)
        .should(times(1))
        .isValid(eq(NEW_EMAIL_IDENTIFICATION_NUMBER));
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
        .body(BodyInserters.fromValue(NEW_EMAIL))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.emailStatus")
        .isEqualTo(EmailStatusEnum.ERROR.name());

    then(checkIdentificationNumberGateway)
        .should(times(1))
        .isValid(eq(NEW_EMAIL_IDENTIFICATION_NUMBER));
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
        .body(BodyInserters.fromValue(NEW_EMAIL))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .isEmpty();

    then(checkIdentificationNumberGateway)
        .should(times(1))
        .isValid(eq(NEW_EMAIL_IDENTIFICATION_NUMBER));
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
        .body(BodyInserters.fromValue(NEW_EMAIL))
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody();

    then(checkIdentificationNumberGateway)
        .should(times(1))
        .isValid(eq(NEW_EMAIL_IDENTIFICATION_NUMBER));
    then(sendEmailToQueueGateway).should(times(1)).sendEmail(emailEntityCaptor.capture());
    assertThat(emailEntityCaptor.getValue()).isEqualTo(NEW_EMAIL_ENTITY.withEmailStatus(null));
  }
}
