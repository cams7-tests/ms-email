/** */
package br.cams7.tests.ms.infra.controller;

import static br.cams7.tests.ms.core.common.PageDTOTestData.PAGE_NUMBER;
import static br.cams7.tests.ms.core.common.PageDTOTestData.PAGE_SIZE;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_ID;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = GetEmailController.class)
class GetEmailControllerMvcTests {

  private static final String SORT_FIELD = "emailFrom";
  private static final Pageable PAGE =
      PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by(SORT_FIELD).ascending());

  @Autowired private MockMvc mockMvc;

  @MockBean private GetEmailController getEmailController;

  @Test
  @DisplayName("getEmails returns emails when successfull")
  void getEmails_ReturnsEmails_WhenSuccessful() throws Exception {
    var defaultPage = PageRequest.of(0, 5, Sort.by("emailId").descending());
    mockMvc
        .perform(get("/emails").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    then(getEmailController).should(times(1)).getEmails(eq(defaultPage));
  }

  @Test
  @DisplayName("getEmails returns emails when pass pagination and sort")
  void getEmails_ReturnsEmails_WhenPassPaginationAndSort() throws Exception {
    mockMvc
        .perform(
            get(
                    "/emails?page={page}&size={size}&sort={sort}&direction={direction}",
                    PAGE_NUMBER,
                    PAGE_SIZE,
                    SORT_FIELD,
                    "asc")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    then(getEmailController).should(times(1)).getEmails(eq(PAGE));
  }

  @Test
  @DisplayName("getEmail returns an email when successfull")
  void getEmail_ReturnsAnEmail_WhenSuccessful() throws Exception {
    mockMvc
        .perform(
            get("/emails/{emailId}", EMAIL_ID.toString()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    then(getEmailController).should(times(1)).getEmail(eq(EMAIL_ID));
  }
}
