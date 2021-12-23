package br.cams7.tests.ms.infra.persistence.repository;

import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_FROM;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_SENT_DATE;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_STATUS;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_TO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_SUBJECT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_TEXT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.OWNER_REF;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.cams7.tests.ms.core.common.PageDTO;
import br.cams7.tests.ms.core.common.PageDTOTestData;
import br.cams7.tests.ms.core.port.out.GetEmailRepository;
import br.cams7.tests.ms.core.port.out.GetEmailsRepository;
import br.cams7.tests.ms.core.port.out.SaveEmailRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailEntityTestData;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;

@DataJpaTest
@Import({SQLEmailRepository.class})
class SQLEmailRepositoryTests {

  private static final PageDTO DEFAULT_PAGE_DTO = PageDTOTestData.defaultPage();
  private static UUID EMAIL_ID = UUID.fromString("fd0622c0-6101-11ec-902c-8f89d045b40c");
  private static UUID WRONG_EMAIL_ID = UUID.fromString("0df7dbda-7277-4d6e-a4e9-ee60bf7cbb05");
  private static EmailEntity DEFAULT_EMAIL_ENTITY = EmailEntityTestData.defaultEmail();

  @Autowired private GetEmailsRepository getEmailsRepository;
  @Autowired private GetEmailRepository getEmailRepository;
  @Autowired private SaveEmailRepository saveEmailRepository;

  @Test
  @DisplayName("findAll returns paged emails when successfull")
  void findAll_ReturnsPagedEmails_WhenSuccessful() {
    var emails = getEmailsRepository.findAll(DEFAULT_PAGE_DTO);

    assertThat(emails).hasSize(DEFAULT_PAGE_DTO.getPageSize());
  }

  @Test
  @DisplayName("findAll throws error when pass null page")
  void findAll_ThrowsError_WhenPassNullPage() {
    assertThrows(
        NullPointerException.class,
        () -> {
          getEmailsRepository.findAll(null);
        });
  }

  @Test
  @DisplayName("findAll returns no emails when \"page number\" is 2 and \"page size\" is 10")
  void findAll_ReturnsNoEmails_WhenPageNumberIs2AndPageSizeIs10() {
    var page = PageDTOTestData.defaultPage();
    page.setPageNumber(2);

    var emails = getEmailsRepository.findAll(page);

    assertThat(emails).isEmpty();
  }

  @Test
  @DisplayName("findAll returns one email when \"page number\" is 2 and \"page size\" is 5")
  void findAll_ReturnsOneEmail_WhenPageNumberIs2AndPageSizeIs5() {
    var page = PageDTOTestData.defaultPage();
    page.setPageNumber(2);
    page.setPageSize(5);

    var emails = getEmailsRepository.findAll(page);

    assertThat(emails).isNotEmpty();
    assertThat(emails.size() >= 1 && emails.size() <= 2).isTrue();
  }

  @Test
  @DisplayName("findById returns an email when successfull")
  void findById_ReturnsAnEmail_WhenSuccessful() {
    var email = getEmailRepository.findById(EMAIL_ID);

    assertThat(email).isPresent();
    assertThat(email.get().getEmailId()).isEqualTo(EMAIL_ID);
  }

  @Test
  @DisplayName("findById throws error when pass null \"email id\"")
  void findById_ThrowsError_WhenPassNullEmailId() {
    assertThrows(
        InvalidDataAccessApiUsageException.class,
        () -> {
          getEmailRepository.findById(null);
        });
  }

  @Test
  @DisplayName("findById returns no email when pass wrong \"email id\"")
  void findById_ReturnsNoEmail_WhenPassWrongEmailId() {
    var email = getEmailRepository.findById(WRONG_EMAIL_ID);

    assertThat(email).isNotPresent();
  }

  @Test
  @DisplayName("save creates an email when successfull")
  void save_CreatesAnEmail_WhenSuccessful() {
    var email = saveEmailRepository.save(DEFAULT_EMAIL_ENTITY.withEmailId(null));

    assertThat(email).isNotNull();
    assertThat(email.getEmailId()).isNotNull();
    assertThat(email.getOwnerRef()).isEqualTo(OWNER_REF);
    assertThat(email.getEmailFrom()).isEqualTo(EMAIL_FROM);
    assertThat(email.getEmailTo()).isEqualTo(EMAIL_TO);
    assertThat(email.getSubject()).isEqualTo(MESSAGE_SUBJECT);
    assertThat(email.getText()).isEqualTo(MESSAGE_TEXT);
    assertThat(email.getEmailSentDate()).isEqualTo(EMAIL_SENT_DATE);
    assertThat(email.getEmailStatus()).isEqualTo(EMAIL_STATUS);
  }

  @Test
  @DisplayName("save throws error when pass null email")
  void save_ThrowsError_WhenPassNullEmail() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          saveEmailRepository.save(null);
        });
  }
}
