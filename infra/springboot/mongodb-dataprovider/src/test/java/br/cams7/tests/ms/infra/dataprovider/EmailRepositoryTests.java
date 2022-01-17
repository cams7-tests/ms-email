package br.cams7.tests.ms.infra.dataprovider;

import static br.cams7.tests.ms.core.port.pagination.OrderDTOTestData.getOrderDTO;
import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.PAGE_NUMBER;
import static br.cams7.tests.ms.core.port.pagination.PageDTOTestData.PAGE_SIZE;
import static br.cams7.tests.ms.domain.EmailEntityTestData.getEmailEntity;
import static br.cams7.tests.ms.infra.common.FileUtils.getContentFile;
import static br.cams7.tests.ms.infra.dataprovider.model.EmailModel.COLLECTION;
import static br.cams7.tests.ms.infra.dataprovider.model.EmailModel.FIELD_EMAIL_FROM;
import static br.cams7.tests.ms.infra.dataprovider.model.EmailModel.FIELD_EMAIL_ID;
import static br.cams7.tests.ms.infra.dataprovider.model.EmailModel.FIELD_EMAIL_SENT_DATE;
import static br.cams7.tests.ms.infra.dataprovider.model.EmailModel.FIELD_EMAIL_STATUS;
import static br.cams7.tests.ms.infra.dataprovider.model.EmailModel.FIELD_EMAIL_TO;
import static br.cams7.tests.ms.infra.dataprovider.model.EmailModel.FIELD_OWNER_REF;
import static br.cams7.tests.ms.infra.dataprovider.model.EmailModel.FIELD_SUBJECT;
import static br.cams7.tests.ms.infra.dataprovider.model.EmailModel.FIELD_TEXT;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static reactor.test.StepVerifier.create;

import br.cams7.tests.ms.core.port.out.GetEmailGateway;
import br.cams7.tests.ms.core.port.out.GetEmailsGateway;
import br.cams7.tests.ms.core.port.out.SaveEmailGateway;
import br.cams7.tests.ms.core.port.pagination.OrderDTO;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailStatusEnum;
import br.cams7.tests.ms.infra.dataprovider.model.EmailModel;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;

import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@SpringBootConfiguration
@EnableAutoConfiguration
@DataMongoTest(properties = {"spring.mongodb.embedded.version=3.5.5"})
@Import({EmailRepository.class})
public class EmailRepositoryTests {

  private static String EMAIL_ID = "61e2b7c6adf831316b1edba6";
  private static String WRONG_EMAIL_ID = "61e2d5838328b90aae3edeeb";
  private static EmailEntity DEFAULT_EMAIL_ENTITY = getEmailEntity();
  private static final OrderDTO DEFAULT_ORDER_DTO = getOrderDTO();

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

  @Autowired private ReactiveMongoOperations mongoTemplate;

  @Value("classpath:email-data.json")
  private Resource dataResource;

  @TestConfiguration
  static class SQLEmailRepositoryTestsContextConfiguration {
    @Bean
    ModelMapper modelMapper() {
      return new ModelMapper();
    }
  }
  
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

  @BeforeEach
  void setUp() {
    create(
            mongoTemplate
                .dropCollection(COLLECTION)
                .thenMany(Flux.fromIterable(getEmails(dataResource)).flatMap(mongoTemplate::save))
                .then())
        .expectSubscription()
        .verifyComplete();
  }

  @Test
  @DisplayName("findAll returns paged emails when successfull")
  void findAll_ReturnsPagedEmails_WhenSuccessful() {
    var response = getEmailsGateway.findAll(PAGE_NUMBER, PAGE_SIZE, List.of(DEFAULT_ORDER_DTO));

    create(response)
        .expectSubscription()
        .expectNextMatches(
            page -> {
              System.out.println(page);
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
  @DisplayName("findAll throws error when pass null page")
  void findAll_ThrowsError_WhenPassNullPage() {
    assertThrows(
        NullPointerException.class,
        () -> {
          getEmailsGateway.findAll(0, 0, null);
        });
  }

  @Test
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
  @DisplayName("findById throws error when pass null \"email id\"")
  void findById_ThrowsError_WhenPassNullEmailId() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          getEmailGateway.findById(null);
        });
  }

  @Test
  @DisplayName("findById returns no email when pass wrong \"email id\"")
  void findById_ReturnsNoEmail_WhenPassWrongEmailId() {
    var response = getEmailGateway.findById(WRONG_EMAIL_ID);

    create(response).expectSubscription().expectNextCount(0).verifyComplete();
  }

  @Test
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
  @DisplayName("save throws error when pass null email")
  void save_ThrowsError_WhenPassNullEmail() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          saveEmailGateway.save(null);
        });
  }

  private static List<EmailModel> getEmails(Resource dataResource) {
    JsonParser parser = JsonParserFactory.getJsonParser();
    try {
      return parser.parseList(getContentFile(dataResource)).stream()
          .map(
              object -> {
                @SuppressWarnings("unchecked")
                var jsonObject = (Map<String, Object>) object;
                EmailModel email = new EmailModel();
                email.setId((String) jsonObject.get(FIELD_EMAIL_ID));
                email.setOwnerRef((String) jsonObject.get(FIELD_OWNER_REF));
                email.setEmailFrom((String) jsonObject.get(FIELD_EMAIL_FROM));
                email.setEmailTo((String) jsonObject.get(FIELD_EMAIL_TO));
                email.setSubject((String) jsonObject.get(FIELD_SUBJECT));
                email.setText((String) jsonObject.get(FIELD_TEXT));
                email.setEmailSentDate(
                    LocalDateTime.parse(
                        (String) jsonObject.get(FIELD_EMAIL_SENT_DATE),
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                email.setEmailStatus(
                    EmailStatusEnum.valueOf((String) jsonObject.get(FIELD_EMAIL_STATUS)));
                return email;
              })
          .collect(Collectors.toList());
    } catch (IOException e) {
      System.err.println(e);
    }

    return List.of();
  }
}
