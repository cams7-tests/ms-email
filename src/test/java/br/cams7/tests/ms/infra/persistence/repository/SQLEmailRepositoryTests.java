package br.cams7.tests.ms.infra.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import br.cams7.tests.ms.core.common.PageDTO;
import br.cams7.tests.ms.core.common.PageDTOTestData;
import br.cams7.tests.ms.core.port.out.GetEmailsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({SQLEmailRepository.class})
public class SQLEmailRepositoryTests {

  private static final PageDTO DEFAULT_PAGE_DTO = PageDTOTestData.defaultPage();
  private static final int TOTAL_EMAILS = 10;

  @Autowired private GetEmailsRepository getEmailsRepository;

  @Test
  @DisplayName("findAll returns paged emails when successfull")
  void findAll_ReturnsPagedEmails_WhenSuccessful() {
    var emails = getEmailsRepository.findAll(DEFAULT_PAGE_DTO);
    assertThat(emails).isNotEmpty();
    assertThat(emails.size()).isEqualTo(TOTAL_EMAILS);
  }
}
