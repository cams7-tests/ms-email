package br.cams7.tests.ms.infra.entrypoint;

import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.FIRST_EMAIL_FROM;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.FIRST_EMAIL_ID;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.FIRST_EMAIL_IDENTIFICATION_NUMBER;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.FIRST_EMAIL_SUBJECT;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.FIRST_EMAIL_TEXT;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.FIRST_EMAIL_TO;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.INVALID_EMAIL_ID;
import static br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTOTestData.LAST_EMAIL_ID;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import br.cams7.tests.ms.domain.EmailStatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = {
      "spring.r2dbc.url=r2dbc:h2:mem:///get-email-controller-tests?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
    })
@AutoConfigureWebTestClient
class GetEmailControllerIntegrationTests {

  @Autowired private WebTestClient testClient;

  @Test
  @DisplayName("Returns all emails when pass 'page number' is 0 and 'page size' is 20")
  void getEmails_ReturnsAllEmails_WhenPassPageNumberIs0AndPageSizeIs20() {
    testClient
        .get()
        .uri("/emails?page=0&size=20&sort=emailSentDate,desc")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.content[0].emailId")
        .isEqualTo(FIRST_EMAIL_ID)
        .jsonPath("$.content[10].emailId")
        .isEqualTo(LAST_EMAIL_ID)
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

  @Test
  @DisplayName("Returns sorted emails when pass 'email sent date' ascending")
  void getEmails_ReturnsSortedEmails_WhenPassPageEmailSentDateAscending() {
    testClient
        .get()
        .uri("/emails?page=0&size=15&sort=emailSentDate,asc")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.content[10].emailId")
        .isEqualTo(FIRST_EMAIL_ID)
        .jsonPath("$.content[0].emailId")
        .isEqualTo(LAST_EMAIL_ID)
        .jsonPath("$.size")
        .isEqualTo(15);
  }

  @Test
  @DisplayName("Returns no emails when pass 'page number' is 2 and 'page size' is 10")
  void getEmails_ReturnsNoEmails_WhenPassPageNumberIs2AndPageSizeIs10() {
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
  @DisplayName("Returns an email when pass a valid id")
  void getEmail_ReturnsAnEmail_WhenPassAValidId() {
    testClient
        .get()
        .uri("/emails/{id}", FIRST_EMAIL_ID)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.emailId")
        .isEqualTo(FIRST_EMAIL_ID)
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

  @Test
  @DisplayName("Returns error when pass a invalid id")
  void getEmail_ReturnsError_WhenPassAInvalidId() {
    testClient.get().uri("/emails/{id}", INVALID_EMAIL_ID).exchange().expectStatus().isBadRequest();
  }
}
