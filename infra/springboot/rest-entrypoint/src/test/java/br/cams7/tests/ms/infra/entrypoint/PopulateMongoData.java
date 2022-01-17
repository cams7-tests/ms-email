package br.cams7.tests.ms.infra.entrypoint;

import static br.cams7.tests.ms.infra.common.FileUtils.getContentFile;
import static br.cams7.tests.ms.infra.common.ProfileUtils.REST_RABBITMQ_MONOGODB_PROFILE;
import static java.util.logging.Level.SEVERE;
import static reactor.test.StepVerifier.create;

import br.cams7.tests.ms.domain.EmailStatusEnum;
import br.cams7.tests.ms.infra.dataprovider.model.EmailModel;
import br.cams7.tests.ms.infra.dataprovider.model.UserModel;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Log
@Profile(REST_RABBITMQ_MONOGODB_PROFILE)
@Component
public class PopulateMongoData implements CommandLineRunner {

  public static final String USER_COLLECTION = "user";
  public static final String USER_NAME = "name";
  public static final String USER_USERNAME = "username";
  public static final String USER_PASSWORD = "password";
  public static final String USER_AUTHORITIES = "authorities";

  public static final String EMAIL_COLLECTION = "email";
  public static final String EMAIL_ID = "_id";
  public static final String EMAIL_OWNER_REF = "owner_ref";
  public static final String EMAIL_EMAIL_FROM = "email_from";
  public static final String EMAIL_EMAIL_TO = "email_to";
  public static final String EMAIL_SUBJECT = "subject";
  public static final String EMAIL_TEXT = "text";
  public static final String EMAIL_EMAIL_SENT_DATE = "email_sent_date";
  public static final String EMAIL_EMAIL_STATUS = "email_status";

  public static final long TOTAL_EMAILS = 11l;
  public static long TIMEOUT_IN_SECOUNDS = 10l;

  @Autowired private ReactiveMongoOperations mongoTemplate;

  @Value("classpath:user-data.json")
  private Resource userDataResource;

  @Value("classpath:email-data.json")
  private Resource emailDataResource;

  @Override
  public void run(String... args) throws Exception {
    log.info("--------------------------------------------------");
    log.info("Mongo has been populated");
    log.info("--------------------------------------------------");

    create(
            mongoTemplate
                .dropCollection(USER_COLLECTION)
                .thenMany(
                    Flux.fromIterable(getUsers(userDataResource)).flatMap(mongoTemplate::save))
                .thenMany(
                    mongoTemplate
                        .dropCollection(EMAIL_COLLECTION)
                        .thenMany(
                            Flux.fromIterable(getEmails(emailDataResource))
                                .flatMap(mongoTemplate::save)))
                .then())
        .expectSubscription()
        .verifyComplete();
  }

  private static List<EmailModel> getEmails(Resource dataResource) {
    JsonParser parser = JsonParserFactory.getJsonParser();
    try {
      return parser.parseList(getContentFile(dataResource)).stream()
          .map(
              object -> {
                @SuppressWarnings("unchecked")
                var jsonObject = (Map<String, Object>) object;
                var email = new EmailModel();
                email.setEmailId((String) jsonObject.get(EMAIL_ID));
                email.setOwnerRef((String) jsonObject.get(EMAIL_OWNER_REF));
                email.setEmailFrom((String) jsonObject.get(EMAIL_EMAIL_FROM));
                email.setEmailTo((String) jsonObject.get(EMAIL_EMAIL_TO));
                email.setSubject((String) jsonObject.get(EMAIL_SUBJECT));
                email.setText((String) jsonObject.get(EMAIL_TEXT));
                email.setEmailSentDate(
                    LocalDateTime.parse(
                        (String) jsonObject.get(EMAIL_EMAIL_SENT_DATE),
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                email.setEmailStatus(
                    EmailStatusEnum.valueOf((String) jsonObject.get(EMAIL_EMAIL_STATUS)));
                return email;
              })
          .collect(Collectors.toList());
    } catch (IOException e) {
      log.log(SEVERE, "An error occurred while load emails from json file", e.getCause());
    }

    return List.of();
  }

  private static List<UserModel> getUsers(Resource dataResource) {
    JsonParser parser = JsonParserFactory.getJsonParser();
    try {
      return parser.parseList(getContentFile(dataResource)).stream()
          .map(
              object -> {
                @SuppressWarnings("unchecked")
                var jsonObject = (Map<String, Object>) object;
                var email = new UserModel();
                email.setAuthorities((String) jsonObject.get(USER_AUTHORITIES));
                email.setName((String) jsonObject.get(USER_NAME));
                email.setPassword((String) jsonObject.get(USER_PASSWORD));
                email.setUsername((String) jsonObject.get(USER_USERNAME));
                return email;
              })
          .collect(Collectors.toList());
    } catch (IOException e) {
      log.log(SEVERE, "An error occurred while load users from json file", e.getCause());
    }

    return List.of();
  }
}
