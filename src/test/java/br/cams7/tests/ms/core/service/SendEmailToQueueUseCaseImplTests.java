package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.port.in.EmailVOTestData.defaultEmailVO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.defaultEmailEntity;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static reactor.test.StepVerifier.create;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.exception.InvalidIdentificationNumberException;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberService;
import br.cams7.tests.ms.core.port.out.SendEmailToQueueService;
import br.cams7.tests.ms.domain.EmailEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class SendEmailToQueueUseCaseImplTests {

  private static final EmailVO DEFAULT_EMAIL_VO = defaultEmailVO();
  private static final boolean IS_VALID_IDENTIFICATION_NUMBER = true;
  private static final boolean IS_INVALID_IDENTIFICATION_NUMBER = false;
  private static final EmailEntity DEFAULT_EMAIL_ENTITY = defaultEmailEntity();
  private static final String ERROR_MESSAGE = "Error";

  @InjectMocks private SendEmailToQueueUseCaseImpl sendEmailToQueueUseCase;

  @Spy private ModelMapper modelMapper = new ModelMapper();
  @Mock private SendEmailToQueueService sendEmailService;
  @Mock private CheckIdentificationNumberService checkIdentificationNumberService;

  @Test
  @DisplayName("sendEmail when successfull")
  void sendEmail_WhenSuccessful() {
    given(checkIdentificationNumberService.isValid(anyString()))
        .willReturn(Mono.just(IS_VALID_IDENTIFICATION_NUMBER));
    willDoNothing().given(sendEmailService).sendEmail(any(EmailEntity.class));

    var newEmail =
        DEFAULT_EMAIL_ENTITY.withEmailId(null).withEmailSentDate(null).withEmailStatus(null);

    create(sendEmailToQueueUseCase.sendEmail(DEFAULT_EMAIL_VO))
        .expectSubscription()
        .expectNextCount(0)
        .verifyComplete();

    then(checkIdentificationNumberService)
        .should(times(1))
        .isValid(eq(DEFAULT_EMAIL_VO.getIdentificationNumber()));
    then(sendEmailService).should(times(1)).sendEmail(eq(newEmail));
  }

  @Test
  @DisplayName("sendEmail throws error when pass null email vo")
  void sendEmail_ThrowsError_WhenPassNullEmailVo() {
    assertThrows(
        NullPointerException.class,
        () -> {
          sendEmailToQueueUseCase.sendEmail(null);
        });

    then(checkIdentificationNumberService).should(never()).isValid(anyString());
    then(sendEmailService).should(never()).sendEmail(any(EmailEntity.class));
  }

  @Test
  @DisplayName("sendEmail returns error when invalid identification number")
  void sendEmail_ReturnsError_WhenInvalidIdentificationNumber() {
    given(checkIdentificationNumberService.isValid(anyString()))
        .willReturn(Mono.just(IS_INVALID_IDENTIFICATION_NUMBER));

    create(sendEmailToQueueUseCase.sendEmail(DEFAULT_EMAIL_VO))
        .expectSubscription()
        .expectError(InvalidIdentificationNumberException.class)
        .verify();

    then(checkIdentificationNumberService)
        .should(times(1))
        .isValid(eq(DEFAULT_EMAIL_VO.getIdentificationNumber()));
    then(sendEmailService).should(never()).sendEmail(any(EmailEntity.class));
  }

  @Test
  @DisplayName("sendEmail returns error when some error happened during send email")
  void sendEmail_ReturnsError_WhenSomeErrorHappenedDuringSendEmail() {
    given(checkIdentificationNumberService.isValid(anyString()))
        .willReturn(Mono.just(IS_VALID_IDENTIFICATION_NUMBER));

    var newEmail =
        DEFAULT_EMAIL_ENTITY.withEmailId(null).withEmailSentDate(null).withEmailStatus(null);
    willThrow(new RuntimeException(ERROR_MESSAGE))
        .given(sendEmailService)
        .sendEmail(any(EmailEntity.class));

    create(sendEmailToQueueUseCase.sendEmail(DEFAULT_EMAIL_VO))
        .expectSubscription()
        .expectError(RuntimeException.class)
        .verify();

    then(checkIdentificationNumberService)
        .should(times(1))
        .isValid(eq(DEFAULT_EMAIL_VO.getIdentificationNumber()));
    then(sendEmailService).should(times(1)).sendEmail(eq(newEmail));
  }
}
