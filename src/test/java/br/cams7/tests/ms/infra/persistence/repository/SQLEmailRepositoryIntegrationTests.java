package br.cams7.tests.ms.infra.persistence.repository;

import static br.cams7.tests.ms.core.port.pagination.OrderDTOTestData.defaultOrderDTO;
import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.PAGE_NUMBER;
import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.PAGE_SIZE;
import static br.cams7.tests.ms.domain.EmailEntityTestData.defaultEmailEntity;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static reactor.test.StepVerifier.create;

import br.cams7.tests.ms.core.port.out.GetEmailGateway;
import br.cams7.tests.ms.core.port.out.GetEmailsGateway;
import br.cams7.tests.ms.core.port.out.SaveEmailGateway;
import br.cams7.tests.ms.core.port.pagination.OrderDTO;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@DataR2dbcTest
@Import({SQLEmailRepository.class})
@TestMethodOrder(OrderAnnotation.class)
class SQLEmailRepositoryIntegrationTests {

  private static UUID EMAIL_ID = UUID.fromString("fd0622c0-6101-11ec-902c-8f89d045b40c");
  private static UUID WRONG_EMAIL_ID = UUID.fromString("0df7dbda-7277-4d6e-a4e9-ee60bf7cbb05");
  private static EmailEntity DEFAULT_EMAIL_ENTITY = defaultEmailEntity();
  private static final OrderDTO DEFAULT_ORDER_DTO = defaultOrderDTO();

  private static final int TOTAL_PAGES = 3;
  private static final long TOTAL_ELEMENTS = 11;
  private static final int NUMBER_OF_ELEMENTS = 5;
  private static final boolean HAS_NO_CONTENT = false;
  private static final boolean FIRST = true;
  private static final boolean LAST = false;
  private static final boolean HAS_NEXT = true;
  private static final boolean HAS_PREVIOUS = false;
  private static final boolean SORTED = true;
  private static final boolean IS_EMPTY_ORDERS = false;
  private static final int TOTAL_ORDERS = 1;

  @Autowired private GetEmailsGateway getEmailsGateway;
  @Autowired private GetEmailGateway getEmailGateway;
  @Autowired private SaveEmailGateway saveEmailGateway;

  @TestConfiguration
  static class SQLEmailRepositoryTestsContextConfiguration {
    @Bean
    ModelMapper modelMapper() {
      return new ModelMapper();
    }
  }

  @Test
  @Order(1)
  @DisplayName("findAll returns paged emails when successfull")
  void findAll_ReturnsPagedEmails_WhenSuccessful() {
    var response = getEmailsGateway.findAll(PAGE_NUMBER, PAGE_SIZE, List.of(DEFAULT_ORDER_DTO));

    create(response)
        .expectSubscription()
        .expectNextMatches(
            page -> {
              if (page.getTotalPages() != TOTAL_PAGES) return false;
              if (page.getTotalElements() != TOTAL_ELEMENTS) return false;
              if (page.getNumber() != PAGE_NUMBER) return false;
              if (page.getSize() != PAGE_SIZE) return false;
              if (page.getNumberOfElements() != NUMBER_OF_ELEMENTS) return false;

              var content = page.getContent();
              if (content == null
                  || content.isEmpty() != HAS_NO_CONTENT
                  || content.size() != NUMBER_OF_ELEMENTS) return false;

              if (page.isFirst() != FIRST) return false;
              if (page.isLast() != LAST) return false;
              if (page.isHasNext() != HAS_NEXT) return false;
              if (page.isHasPrevious() != HAS_PREVIOUS) return false;

              var sort = page.getSort();
              if (sort == null || sort.isSorted() != SORTED) return false;

              var orders = sort.getOrders();
              if (orders == null
                  || orders.isEmpty() != IS_EMPTY_ORDERS
                  || orders.size() != TOTAL_ORDERS) return false;

              if (!DEFAULT_ORDER_DTO.equals(orders.get(0))) return false;

              return true;
            })
        .verifyComplete();
  }

  @Test
  @Order(2)
  @DisplayName("findAll throws error when pass null page")
  void findAll_ThrowsError_WhenPassNullPage() {
    assertThrows(
        NullPointerException.class,
        () -> {
          getEmailsGateway.findAll(0, 0, null);
        });
  }

  @Test
  @Order(3)
  @DisplayName("findAll returns no emails when \"page number\" is 3 and \"page size\" is 5")
  void findAll_ReturnsNoEmails_WhenPageNumberIs2AndPageSizeIs10() {
    var pageNumber = 3;
    var numberOfElements = 0;
    var hasNoContent = true;
    var first = false;
    var last = true;
    var hasNext = false;
    var hasPrevious = true;

    var response = getEmailsGateway.findAll(pageNumber, PAGE_SIZE, List.of(DEFAULT_ORDER_DTO));

    create(response)
        .expectSubscription()
        .expectNextMatches(
            page -> {
              if (page.getNumber() != pageNumber) return false;
              if (page.getNumberOfElements() != numberOfElements) return false;
              if (page.getContent() == null || page.getContent().isEmpty() != hasNoContent)
                return false;
              if (page.isFirst() != first) return false;
              if (page.isLast() != last) return false;
              if (page.isHasNext() != hasNext) return false;
              if (page.isHasPrevious() != hasPrevious) return false;
              return true;
            })
        .verifyComplete();
  }

  @Test
  @Order(4)
  @DisplayName("findAll returns one email when \"page number\" is 2 and \"page size\" is 5")
  void findAll_ReturnsOneEmail_WhenPageNumberIs2AndPageSizeIs5() {
    var pageNumber = 2;
    var numberOfElements = 1;
    var first = false;
    var last = true;
    var hasNext = false;
    var hasPrevious = true;

    var response = getEmailsGateway.findAll(pageNumber, PAGE_SIZE, List.of(DEFAULT_ORDER_DTO));

    create(response)
        .expectSubscription()
        .expectNextMatches(
            page -> {
              if (page.getNumber() != pageNumber) return false;
              if (page.getNumberOfElements() != numberOfElements) return false;
              if (page.getContent() == null
                  || page.getContent().isEmpty() != HAS_NO_CONTENT
                  || page.getContent().size() != numberOfElements) return false;
              if (page.isFirst() != first) return false;
              if (page.isLast() != last) return false;
              if (page.isHasNext() != hasNext) return false;
              if (page.isHasPrevious() != hasPrevious) return false;

              return true;
            })
        .verifyComplete();
  }

  @Test
  @Order(5)
  @DisplayName("findById returns an email when successfull")
  void findById_ReturnsAnEmail_WhenSuccessful() {
    var response = getEmailGateway.findById(EMAIL_ID);

    create(response)
        .expectSubscription()
        .expectNextMatches(
            email -> {
              if (!EMAIL_ID.equals(email.getEmailId())) return false;
              return true;
            })
        .verifyComplete();
  }

  @Test
  @Order(6)
  @DisplayName("findById throws error when pass null \"email id\"")
  void findById_ThrowsError_WhenPassNullEmailId() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          getEmailGateway.findById(null);
        });
  }

  @Test
  @Order(7)
  @DisplayName("findById returns no email when pass wrong \"email id\"")
  void findById_ReturnsNoEmail_WhenPassWrongEmailId() {
    var response = getEmailGateway.findById(WRONG_EMAIL_ID);

    create(response).expectSubscription().expectNextCount(0).verifyComplete();
  }

  @Test
  @Order(8)
  @DisplayName("save creates an email when successfull")
  void save_CreatesAnEmail_WhenSuccessful() {
    var response = saveEmailGateway.save(DEFAULT_EMAIL_ENTITY.withEmailId(null));

    create(response)
        .expectSubscription()
        .expectNextMatches(
            email -> {
              if (!DEFAULT_EMAIL_ENTITY.withEmailId(null).equals(email.withEmailId(null)))
                return false;
              return true;
            })
        .verifyComplete();
  }

  @Test
  @Order(9)
  @DisplayName("save throws error when pass null email")
  void save_ThrowsError_WhenPassNullEmail() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          saveEmailGateway.save(null);
        });
  }
}
