/** */
package br.cams7.tests.ms.infra.controller;

import static br.cams7.tests.ms.core.port.in.EmailVOTestData.defaultEmailVO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.defaultEmailEntity;
import static br.cams7.tests.ms.infra.controller.SendEmailRequestDTOTestData.defaultSendEmailRequestDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import br.cams7.tests.ms.core.port.in.SendEmailToQueueUseCase;
import br.cams7.tests.ms.domain.EmailEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class SendEmailControllerTests {

  private static final SendEmailRequestDTO SEND_EMAIL_REQUEST_DTO = defaultSendEmailRequestDTO();
  private static final EmailEntity DEFAULT_EMAIL_ENTITY = defaultEmailEntity();
  private static final EmailVO DEFAULT_EMAIL_VO = defaultEmailVO();

  @InjectMocks private SendEmailController sendEmailController;

  @Mock private SendEmailDirectlyUseCase sendEmailDirectlyUseCase;
  @Mock private SendEmailToQueueUseCase sendEmailToQueueUseCase;

  @Captor private ArgumentCaptor<EmailVO> emailVOCaptor;

  @Test
  @DisplayName("sendEmailDirectly returns email when successfull")
  void sendEmailDirectly_ReturnsEmail_WhenSuccessful() {

    given(sendEmailDirectlyUseCase.sendEmail(any(EmailVO.class))).willReturn(DEFAULT_EMAIL_ENTITY);

    var response = sendEmailController.sendEmailDirectly(SEND_EMAIL_REQUEST_DTO);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.hasBody()).isTrue();

    var email = response.getBody();
    assertThat(email).isNotNull();
    assertThat(email).isEqualTo(DEFAULT_EMAIL_ENTITY);

    then(sendEmailDirectlyUseCase).should().sendEmail(emailVOCaptor.capture());
    assertThat(emailVOCaptor.getValue()).isEqualTo(DEFAULT_EMAIL_VO);
  }

  @Test
  @DisplayName("sendEmailToQueue when successfull")
  void sendEmailToQueue_WhenSuccessful() {

    willDoNothing().given(sendEmailToQueueUseCase).sendEmail(any(EmailVO.class));

    var response = sendEmailController.sendEmailToQueue(SEND_EMAIL_REQUEST_DTO);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.hasBody()).isFalse();

    then(sendEmailToQueueUseCase).should().sendEmail(emailVOCaptor.capture());
    assertThat(emailVOCaptor.getValue()).isEqualTo(DEFAULT_EMAIL_VO);
  }
}
