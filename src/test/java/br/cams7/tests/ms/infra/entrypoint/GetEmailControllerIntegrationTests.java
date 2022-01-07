package br.cams7.tests.ms.infra.entrypoint;

import static br.cams7.tests.ms.infra.entrypoint.EmailResponseCreator.FIRST_EMAIL;
import static br.cams7.tests.ms.infra.entrypoint.EmailResponseCreator.FIRST_EMAIL_ID;
import static br.cams7.tests.ms.infra.entrypoint.EmailResponseCreator.INVALID_EMAIL_ID;
import static br.cams7.tests.ms.infra.entrypoint.EmailResponseCreator.LAST_EMAIL_ID;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
@TestMethodOrder(OrderAnnotation.class)
class GetEmailControllerIntegrationTests {

  @Autowired private WebTestClient testClient;

  @Test
  @Order(1)
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
  @Order(2)
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
  @Order(3)
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
  @Order(4)
  @DisplayName("Returns an email when pass a valid id")
  void getEmail_ReturnsAnEmail_WhenPassAValidId() {
    testClient
        .get()
        .uri("/emails/{id}", FIRST_EMAIL_ID)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(EmailResponseDTO.class)
        .isEqualTo(FIRST_EMAIL);
  }

  @Test
  @Order(5)
  @DisplayName("Returns error when pass a invalid id")
  void getEmail_ReturnsError_WhenPassAInvalidId() {
    testClient.get().uri("/emails/{id}", INVALID_EMAIL_ID).exchange().expectStatus().isBadRequest();
  }
}
