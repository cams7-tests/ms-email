package br.cams7.tests.ms.infra.entrypoint;

import static br.cams7.tests.ms.infra.common.ProfileUtils.REST_RABBITMQ_MONOGODB_PROFILE;
import static br.cams7.tests.ms.infra.common.ProfileUtils.REST_RABBITMQ_POSTGRES_PROFILE;
import static br.cams7.tests.ms.infra.common.ProfileUtils.getActiveProfile;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.FIRST_EMAIL_FROM;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.FIRST_EMAIL_IDENTIFICATION_NUMBER;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.FIRST_EMAIL_ID_MONGO;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.FIRST_EMAIL_ID_UUID;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.FIRST_EMAIL_SUBJECT;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.FIRST_EMAIL_TEXT;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.FIRST_EMAIL_TO;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.INVALID_EMAIL_ID_MONGO;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.INVALID_EMAIL_ID_UUID;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.INVALID_UUID;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.LAST_EMAIL_ID_MONGO;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.LAST_EMAIL_ID_UUID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import br.cams7.tests.ms.domain.EmailStatusEnum;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.security.test.context.support.WithUserDetails;
// import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.scheduler.Schedulers;

@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = {
      "spring.r2dbc.url=r2dbc:h2:mem:///get-email-controller-tests?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
      "spring.mongodb.embedded.version=3.5.5"
    })
@AutoConfigureWebTestClient
class GetEmailControllerTests {

  private static final String USER = "user";

  private static String TIMESTAMP_ATTRIBUTE = "$.timestamp";
  private static String PATH_ATTRIBUTE = "$.path";
  private static String ERROR_ATTRIBUTE = "$.error";
  // private static String MESSAGE_ATTRIBUTE = "$.message";
  private static String TRACE_ATTRIBUTE = "$.trace";
  private static String REQUESTID_ATTRIBUTE = "$.requestId";
  private static String EXCEPTION_ATTRIBUTE = "$.exception";

  @Autowired private WebTestClient testClient;

  @Autowired private Environment environment;

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
  @WithUserDetails(USER)
  @DisplayName(
      "getEmails returns all emails when pass 'page number' is 0 and 'page size' is 20 and user is successfull authenticated and has USER role")
  void
      getEmails_ReturnsAllEmails_WhenPassPageNumberIs0AndPageSizeIs20AndUserIsSuccessfullAuthenticatedAndHasUserRole() {
    testClient
        .get()
        .uri("/emails?page=0&size=20&sort=emailSentDate,desc")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.content[0].emailId")
        .isEqualTo(getFirstEmailId())
        .jsonPath("$.content[10].emailId")
        .isEqualTo(getLastEmailId())
        .jsonPath("$.totalPages")
        .isEqualTo(1)
        .jsonPath("$.totalElements")
        .isEqualTo(11)
        .jsonPath("$.number")
        .isEqualTo(0)
        .jsonPath("$.size")
        .isEqualTo(20)
        .jsonPath("$.numberOfElements")
        .isEqualTo(11)
        .jsonPath("$.last")
        .isEqualTo(true)
        .jsonPath("$.first")
        .isEqualTo(true)
        .jsonPath("$.hasNext")
        .isEqualTo(false)
        .jsonPath("$.hasPrevious")
        .isEqualTo(false);
  }

  //    @Test
  //    @DisplayName("getEmails returns unauthorized when user isn't authenticated")
  //    void getEmails_ReturnsUnauthorized_WhenUserIsNotAuthenticated() {
  //      testClient.get().uri("/emails").exchange().expectStatus().isUnauthorized();
  //    }

  @Test
  @WithUserDetails(USER)
  @DisplayName(
      "getEmails returns sorted emails when pass 'email sent date' ascending and user is successfull authenticated and has USER role")
  void
      getEmails_ReturnsSortedEmails_WhenPassPageEmailSentDateAscendingAndUserIsSuccessfullAuthenticatedAndHasUserRole() {
    testClient
        .get()
        .uri("/emails?page=0&size=15&sort=emailSentDate,asc")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.content[10].emailId")
        .isEqualTo(getFirstEmailId())
        .jsonPath("$.content[0].emailId")
        .isEqualTo(getLastEmailId())
        .jsonPath("$.size")
        .isEqualTo(15);
  }

  @Test
  @WithUserDetails(USER)
  @DisplayName(
      "getEmails returns no emails when pass 'page number' is 2 and 'page size' is 10 and user is successfull authenticated and has USER role")
  void
      getEmails_ReturnsNoEmails_WhenPassPageNumberIs2AndPageSizeIs10AndUserIsSuccessfullAuthenticatedAndHasUserRole() {
    testClient
        .get()
        .uri("/emails?page=2&size=10")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.totalPages")
        .isEqualTo(2)
        .jsonPath("$.totalElements")
        .isEqualTo(11)
        .jsonPath("$.number")
        .isEqualTo(2)
        .jsonPath("$.size")
        .isEqualTo(10)
        .jsonPath("$.numberOfElements")
        .isEqualTo(0)
        .jsonPath("$.last")
        .isEqualTo(true)
        .jsonPath("$.first")
        .isEqualTo(false)
        .jsonPath("$.hasNext")
        .isEqualTo(false)
        .jsonPath("$.hasPrevious")
        .isEqualTo(true);
  }

  @Test
  @WithUserDetails(USER)
  @DisplayName(
      "getEmail returns an email when pass a valid id and user is successfull authenticated and has USER role")
  void getEmail_ReturnsAnEmail_WhenPassAValidIdAndUserIsSuccessfullAuthenticatedAndHasUserRole() {
    var firstEmailId = getFirstEmailId();
    testClient
        .get()
        .uri("/emails/{id}", firstEmailId)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.emailId")
        .isEqualTo(firstEmailId)
        .jsonPath("$.identificationNumber")
        .isEqualTo(FIRST_EMAIL_IDENTIFICATION_NUMBER)
        .jsonPath("$.emailFrom")
        .isEqualTo(FIRST_EMAIL_FROM)
        .jsonPath("$.emailTo")
        .isEqualTo(FIRST_EMAIL_TO)
        // FIXME In Github Actions enviroment the datetime is UTC, but in my local enviroment the
        // datetime is local
        // .jsonPath("$.emailSentDate")
        // .isEqualTo("2022-01-06T21:27:30")
        .jsonPath("$.emailStatus")
        .isEqualTo(EmailStatusEnum.SENT.name())
        .jsonPath("$.subject")
        .isEqualTo(FIRST_EMAIL_SUBJECT)
        .jsonPath("$.text")
        .isEqualTo(FIRST_EMAIL_TEXT);
  }

  //    @Test
  //    @DisplayName("getEmail returns unauthorized when user isn't authenticated")
  //    void getEmail_ReturnsUnauthorized_WhenUserIsNotAuthenticated() {
  //      testClient.get().uri("/emails/{id}",
  //   FIRST_EMAIL_ID).exchange().expectStatus().isUnauthorized();
  //    }

  @Test
  @WithUserDetails(USER)
  @DisplayName(
      "getEmail returns error when pass a invalid id and user is successfull authenticated and has USER role")
  void getEmail_ReturnsError_WhenPassAInvalidIdAndUserIsSuccessfullAuthenticatedAndHasUserRole() {
    var invalidEmailId = getInvalidEmailId();
    testClient
        .get()
        .uri("/emails/{id}", invalidEmailId)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .jsonPath(TIMESTAMP_ATTRIBUTE)
        .isNotEmpty()
        .jsonPath(PATH_ATTRIBUTE)
        .isEqualTo("/emails/" + invalidEmailId)
        .jsonPath(ERROR_ATTRIBUTE)
        .isEqualTo("NOT_FOUND")
        // .jsonPath(MESSAGE_ATTRIBUTE)
        // .isNotEmpty()
        .jsonPath(TRACE_ATTRIBUTE)
        .isEmpty()
        .jsonPath(REQUESTID_ATTRIBUTE)
        .isNotEmpty()
        .jsonPath(EXCEPTION_ATTRIBUTE)
        .isEqualTo("br.cams7.tests.ms.core.port.in.exception.ResponseStatusException");
  }

  @Test
  @WithUserDetails(USER)
  @DisplayName(
      "getEmail returns error when pass a invalid UUID and user is successfull authenticated and has USER role")
  void getEmail_ReturnsError_WhenPassAInvalidUuidAndUserIsSuccessfullAuthenticatedAndHasUserRole() {
    assumeTrue(REST_RABBITMQ_POSTGRES_PROFILE.equals(getActiveProfile(environment)));
    testClient
        .get()
        .uri("/emails/{id}", INVALID_UUID)
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody()
        .jsonPath(TIMESTAMP_ATTRIBUTE)
        .isNotEmpty()
        .jsonPath(PATH_ATTRIBUTE)
        .isEqualTo("/emails/" + INVALID_UUID)
        .jsonPath(ERROR_ATTRIBUTE)
        .isEqualTo("Internal Server Error")
        // .jsonPath(MESSAGE_ATTRIBUTE)
        // .isNotEmpty()
        //                .jsonPath(TRACE_ATTRIBUTE)
        //                .isNotEmpty()
        .jsonPath(REQUESTID_ATTRIBUTE)
        .isNotEmpty();
  }

  private String getFirstEmailId() {
    return REST_RABBITMQ_MONOGODB_PROFILE.equals(getActiveProfile(environment))
        ? FIRST_EMAIL_ID_MONGO
        : FIRST_EMAIL_ID_UUID;
  }

  private String getLastEmailId() {
    return REST_RABBITMQ_MONOGODB_PROFILE.equals(getActiveProfile(environment))
        ? LAST_EMAIL_ID_MONGO
        : LAST_EMAIL_ID_UUID;
  }

  private String getInvalidEmailId() {
    return REST_RABBITMQ_MONOGODB_PROFILE.equals(getActiveProfile(environment))
        ? INVALID_EMAIL_ID_MONGO
        : INVALID_EMAIL_ID_UUID;
  }
}
