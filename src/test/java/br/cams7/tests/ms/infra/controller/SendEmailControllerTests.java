/** */
package br.cams7.tests.ms.infra.controller;

import static br.cams7.tests.ms.core.port.in.EmailVOTestData.defaultEmailVO;
import static br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTOTestData.defaultEmailResponseDTO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.defaultEmailEntity;
import static br.cams7.tests.ms.infra.controller.SendEmailRequestDTOTestData.defaultSendEmailRequestDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static reactor.test.StepVerifier.create;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import br.cams7.tests.ms.core.port.in.SendEmailToQueueUseCase;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.infra.controller.mapper.ResponseConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class SendEmailControllerTests {

  private static final SendEmailRequestDTO SEND_EMAIL_REQUEST_DTO = defaultSendEmailRequestDTO();
  private static final EmailEntity DEFAULT_EMAIL_ENTITY = defaultEmailEntity();
  private static final EmailResponseDTO DEFAULT_EMAIL_RESPONSE_DTO = defaultEmailResponseDTO();
  private static final EmailVO DEFAULT_EMAIL_VO = defaultEmailVO();

  @InjectMocks private SendEmailController sendEmailController;

  @Mock private SendEmailDirectlyUseCase sendEmailDirectlyUseCase;
  @Mock private SendEmailToQueueUseCase sendEmailToQueueUseCase;
  @Mock private ResponseConverter responseConverter;

  @Captor private ArgumentCaptor<EmailVO> emailVOCaptor;

  @Test
  @DisplayName("sendEmailDirectly returns email when successfull")
  void sendEmailDirectly_ReturnsEmail_WhenSuccessful() {

    given(sendEmailDirectlyUseCase.sendEmail(any(EmailVO.class)))
        .willReturn(Mono.just(DEFAULT_EMAIL_ENTITY));
    given(responseConverter.convert(any(EmailEntity.class))).willReturn(DEFAULT_EMAIL_RESPONSE_DTO);

    create(sendEmailController.sendEmailDirectly(SEND_EMAIL_REQUEST_DTO))
        .expectSubscription()
        .expectNext(DEFAULT_EMAIL_RESPONSE_DTO)
        .verifyComplete();

    then(sendEmailDirectlyUseCase).should().sendEmail(emailVOCaptor.capture());
    assertThat(emailVOCaptor.getValue()).isEqualTo(DEFAULT_EMAIL_VO);
    then(responseConverter).should().convert(eq(DEFAULT_EMAIL_ENTITY));
  }

  @Test
  @DisplayName("sendEmailToQueue when successfull")
  void sendEmailToQueue_WhenSuccessful() {

    given(sendEmailToQueueUseCase.sendEmail(any(EmailVO.class))).willReturn(Mono.empty());

    create(sendEmailController.sendEmailToQueue(SEND_EMAIL_REQUEST_DTO))
        .expectSubscription()
        .expectNextCount(0)
        .verifyComplete();

    then(sendEmailToQueueUseCase).should().sendEmail(emailVOCaptor.capture());
    assertThat(emailVOCaptor.getValue()).isEqualTo(DEFAULT_EMAIL_VO);
  }
}
