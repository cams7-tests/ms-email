package br.cams7.tests.ms.infra.entrypoint;

import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_FROM;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_TO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_SUBJECT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_TEXT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.NEW_EMAIL_ENTITY;
import static br.cams7.tests.ms.domain.EmailEntityTestData.OWNER_REF;
import static br.cams7.tests.ms.infra.entrypoint.request.SendEmailRequestDTOTestData.NEW_EMAIL;
import static br.cams7.tests.ms.infra.entrypoint.request.SendEmailRequestDTOTestData.NEW_EMAIL_WITH_EMPTY_SUBJECT;
import static br.cams7.tests.ms.infra.entrypoint.request.SendEmailRequestDTOTestData.NEW_EMAIL_WITH_INVALID_EMAIL_FROM;
import static br.cams7.tests.ms.infra.entrypoint.request.SendEmailRequestDTOTestData.NEW_EMAIL_WITH_INVALID_IDENTIFICATION_NUMBER;
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
import org.springframework.security.test.context.support.WithUserDetails;
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

  private static final String USER = "user";
  private static final String ADMIN = "admin";

  private static final boolean IS_VALID_IDENTIFICATION_NUMBER = true;
  private static final boolean IS_INVALID_IDENTIFICATION_NUMBER = false;

  private static final String ERROR_MESSAGE = "Error";

  @Autowired private WebTestClient testClient;

  @MockBean private CheckIdentificationNumberGateway checkIdentificationNumberGateway;
  @MockBean private SendEmailGateway sendEmailGateway;
  @MockBean private SendEmailToQueueGateway sendEmailToQueueGateway;

  @Captor private ArgumentCaptor<EmailEntity> emailEntityCaptor;

  @Test
  @WithUserDetails(ADMIN)
  @DisplayName(
      "sendEmailDirectly returns a registred email and sent an email directly when pass valid params and user is successfull authenticated and has ADMIN role")
  void
      sendEmailDirectly_ReturnsARegistredEmailAndSendAnEmailDirectly_WhenPassValidParamsAndUserIsSuccessfullAuthenticatedAndHasAdminRole()
          throws SendEmailException {
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
  @WithUserDetails(USER)
  @DisplayName(
      "sendEmailDirectly returns forbidden when user is successfull authenticated and doesn't have ADMIN role")
  void sendEmailDirectly_ReturnsForbidden_WhenUserDoesNotHaveAdminRole() {
    testClient
        .post()
        .uri("/send-email-directly")
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(NEW_EMAIL))
        .exchange()
        .expectStatus()
        .isForbidden();
  }

  @Test
  @DisplayName("sendEmailDirectly returns unauthorized when user isn't authenticated")
  void sendEmailDirectly_ReturnsUnauthorized_WhenUserIsNotAuthenticated() {
    testClient
        .post()
        .uri("/send-email-directly")
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(NEW_EMAIL))
        .exchange()
        .expectStatus()
        .isUnauthorized();
  }

  @Test
  @WithUserDetails(ADMIN)
  @DisplayName(
      "sendEmailDirectly returns error when pass empty subject and user is successfull authenticated and has ADMIN role")
  void
      sendEmailDirectly_ReturnsError_WhenPassEmptySubjectAndUserIsSuccessfullAuthenticatedAndHasAdminRole() {
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
  @WithUserDetails(ADMIN)
  @DisplayName(
      "sendEmailDirectly returns error when pass 'identification number' that isn't a valid CPF and user is successfull authenticated and has ADMIN role")
  void
      sendEmailDirectly_ReturnsError_WhenPassIdentificationNumberThatIsntAValidCpfAndUserIsSuccessfullAuthenticatedAndHasAdminRole() {
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
  @WithUserDetails(ADMIN)
  @DisplayName(
      "sendEmailDirectly returns error when pass invalid 'email from' and user is successfull authenticated and has ADMIN role")
  void
      sendEmailDirectly_ReturnsError_WhenPassInvalidEmailFromAndUserIsSuccessfullAuthenticatedAndHasAdminRole() {
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
  @WithUserDetails(ADMIN)
  @DisplayName(
      "sendEmailDirectly returns error when pass a invalid 'identification number' and user is successfull authenticated and has ADMIN role")
  void
      sendEmailDirectly_ReturnsError_WhenPassAInvalidIdentificationNumberAndUserIsSuccessfullAuthenticatedAndHasAdminRole()
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

    then(checkIdentificationNumberGateway).should(times(1)).isValid(eq(OWNER_REF));
    then(sendEmailGateway).should(never()).sendEmail(any(EmailEntity.class));
  }

  @Test
  @WithUserDetails(ADMIN)
  @DisplayName(
      "sendEmailDirectly returns error when some error happened during 'identification number' validation and user is successfull authenticated and has ADMIN role")
  void
      sendEmailDirectly_ReturnsError_WhenSomeErrorHappenedDuringIdentificationNumberValidationAndUserIsSuccessfullAuthenticatedAndHasAdminRole()
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

    then(checkIdentificationNumberGateway).should(times(1)).isValid(eq(OWNER_REF));
    then(sendEmailGateway).should(never()).sendEmail(any(EmailEntity.class));
  }

  @Test
  @WithUserDetails(ADMIN)
  @DisplayName(
      "sendEmailDirectly returns a registred email with error status and don't sent an email when some error happened during try sent email and user is successfull authenticated and has ADMIN role")
  void
      sendEmailDirectly_ReturnsARegistredEmailWithErrorStatusAndDontSentAndEmail_WhenSomeErrorHappenedDuringTrySendEmailAndUserIsSuccessfullAuthenticatedAndHasAdminRole()
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

    then(checkIdentificationNumberGateway).should(times(1)).isValid(eq(OWNER_REF));
    then(sendEmailGateway).should(times(1)).sendEmail(emailEntityCaptor.capture());
    assertThat(emailEntityCaptor.getValue().withEmailId(null).withEmailSentDate(null))
        .isEqualTo(NEW_EMAIL_ENTITY.withEmailStatus(EmailStatusEnum.ERROR));
  }

  @Test
  @WithUserDetails(ADMIN)
  @DisplayName(
      "sendEmailToQueue returns void and sent an email indirectly when pass valid params and user is successfull authenticated and has ADMIN role")
  void
      sendEmailToQueue_ReturnsVoidAndSendAnEmailIndirectly_WhenPassValidParamsAndUserIsSuccessfullAuthenticatedAndHasAdminRole()
          throws SendEmailException {
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

    then(checkIdentificationNumberGateway).should(times(1)).isValid(eq(OWNER_REF));
    then(sendEmailToQueueGateway).should(times(1)).sendEmail(emailEntityCaptor.capture());
    assertThat(emailEntityCaptor.getValue()).isEqualTo(NEW_EMAIL_ENTITY.withEmailStatus(null));
  }

  @Test
  @WithUserDetails(USER)
  @DisplayName(
      "sendEmailToQueue returns forbidden when user is successfull authenticated and doesn't have ADMIN role")
  void sendEmailToQueue_ReturnsForbidden_WhenUserDoesNotHaveAdminRole() {
    testClient
        .post()
        .uri("/send-email-to-queue")
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(NEW_EMAIL))
        .exchange()
        .expectStatus()
        .isForbidden();
  }

  @Test
  @DisplayName("sendEmailToQueue returns unauthorized when user isn't authenticated")
  void sendEmailToQueue_ReturnsUnauthorized_WhenUserIsNotAuthenticated() {
    testClient
        .post()
        .uri("/send-email-to-queue")
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(NEW_EMAIL))
        .exchange()
        .expectStatus()
        .isUnauthorized();
  }

  @Test
  @WithUserDetails(ADMIN)
  @DisplayName(
      "sendEmailToQueue returns error when some error happened during try sent email to queue and user is successfull authenticated and has ADMIN role")
  void
      sendEmailToQueue_ReturnsError_WhenSomeErrorHappenedDuringTrySentEmailToQueueAndUserIsSuccessfullAuthenticatedAndHasAdminRole()
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

    then(checkIdentificationNumberGateway).should(times(1)).isValid(eq(OWNER_REF));
    then(sendEmailToQueueGateway).should(times(1)).sendEmail(emailEntityCaptor.capture());
    assertThat(emailEntityCaptor.getValue()).isEqualTo(NEW_EMAIL_ENTITY.withEmailStatus(null));
  }
}
