package br.cams7.tests.ms.infra.controller;

import static br.cams7.tests.ms.infra.controller.SendEmailRequestDTOTestData.defaultSendEmailRequestDTO;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = SendEmailController.class)
class SendEmailControllerMvcTests {

  private static final SendEmailRequestDTO DEFAULT_SEND_EMAIL_REQUEST_DTO =
      defaultSendEmailRequestDTO();
  private static final ObjectWriter WRITER = getWriter();

  @Autowired private MockMvc mockMvc;

  @MockBean private SendEmailController sendEmailController;

  @Test
  @DisplayName("sendEmailDirectly returns email when successfull")
  void sendEmailDirectly_ReturnsEmail_WhenSuccessful() throws Exception {
    var requestJson = getRequestJson(DEFAULT_SEND_EMAIL_REQUEST_DTO);

    mockMvc
        .perform(
            post("/send-email-directly")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().isOk());

    then(sendEmailController)
        .should(times(1))
        .sendEmailDirectly(eq(DEFAULT_SEND_EMAIL_REQUEST_DTO));
  }

  @Test
  @DisplayName("sendEmailToQueue when successfull")
  void sendEmailToQueue_WhenSuccessful() throws Exception {
    var requestJson = getRequestJson(DEFAULT_SEND_EMAIL_REQUEST_DTO);

    mockMvc
        .perform(
            post("/send-email-to-queue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().isOk());

    then(sendEmailController).should(times(1)).sendEmailToQueue(eq(DEFAULT_SEND_EMAIL_REQUEST_DTO));
  }

  private static String getRequestJson(SendEmailRequestDTO sendEmailRequest)
      throws JsonProcessingException {
    return WRITER.writeValueAsString(sendEmailRequest);
  }

  private static ObjectWriter getWriter() {
    return new ObjectMapper().writer().withDefaultPrettyPrinter();
  }
}
