package br.cams7.tests.ms.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.EmailVOTestData;
import br.cams7.tests.ms.core.port.in.SendEmailToQueueUseCase;
import br.cams7.tests.ms.core.port.in.exception.InvalidIdentificationNumberException;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberService;
import br.cams7.tests.ms.core.port.out.SendEmailToQueueService;
import br.cams7.tests.ms.core.port.out.exception.SendEmailException;
import br.cams7.tests.ms.domain.EmailEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SendEmailToQueueUseCaseTests {

  private static final EmailVO DEFAULT_EMAIL_VO = EmailVOTestData.defaultEmail();
  private static final boolean IS_VALID_IDENTIFICATION_NUMBER = true;
  private static final boolean IS_INVALID_IDENTIFICATION_NUMBER = false;

  private final SendEmailToQueueService sendEmailService =
      Mockito.mock(SendEmailToQueueService.class);
  private final CheckIdentificationNumberService checkIdentificationNumberService =
      Mockito.mock(CheckIdentificationNumberService.class);

  private final SendEmailToQueueUseCase sendEmailToQueueUseCase =
      new SendEmailToQueueUseCaseImpl(sendEmailService, checkIdentificationNumberService);

  @BeforeEach
  void setUp() throws SendEmailException {
    when(checkIdentificationNumberService.isValid(DEFAULT_EMAIL_VO.getIdentificationNumber()))
        .thenReturn(IS_VALID_IDENTIFICATION_NUMBER);
    doNothing().when(sendEmailService).sendEmail(any(EmailEntity.class));
  }

  @Test
  @DisplayName("sendEmail when successfull")
  void sendEmail_WhenSuccessful() {
    sendEmailToQueueUseCase.sendEmail(DEFAULT_EMAIL_VO);

    verify(checkIdentificationNumberService, times(1))
        .isValid(DEFAULT_EMAIL_VO.getIdentificationNumber());
    verify(sendEmailService, times(1)).sendEmail(any(EmailEntity.class));
  }

  @Test
  @DisplayName("sendEmail throws error when pass null email vo")
  void sendEmail_ThrowsError_WhenPassNullEmailVo() {

    assertThrows(
        NullPointerException.class,
        () -> {
          sendEmailToQueueUseCase.sendEmail(null);
        });

    verify(checkIdentificationNumberService, times(0))
        .isValid(DEFAULT_EMAIL_VO.getIdentificationNumber());
    verify(sendEmailService, times(0)).sendEmail(any(EmailEntity.class));
  }

  @Test
  @DisplayName("sendEmail throws error when invalid identification number")
  void sendEmail_ThrowsError_WhenInvalidIdentificationNumber() {
    when(checkIdentificationNumberService.isValid(DEFAULT_EMAIL_VO.getIdentificationNumber()))
        .thenReturn(IS_INVALID_IDENTIFICATION_NUMBER);

    InvalidIdentificationNumberException thrown =
        assertThrows(
            InvalidIdentificationNumberException.class,
            () -> {
              sendEmailToQueueUseCase.sendEmail(DEFAULT_EMAIL_VO);
            });

    assertThat(thrown.getMessage())
        .isEqualTo(
            String.format(
                "The identification number \"%s\" isn't valid",
                DEFAULT_EMAIL_VO.getIdentificationNumber()));

    verify(checkIdentificationNumberService, times(1))
        .isValid(DEFAULT_EMAIL_VO.getIdentificationNumber());
    verify(sendEmailService, times(0)).sendEmail(any(EmailEntity.class));
  }

  @Test
  @DisplayName("sendEmail throws error when some error happened during send email")
  void sendEmail_ThrowsError_WhenSomeErrorHappenedDuringSendEmail() {
    doThrow(new RuntimeException()).when(sendEmailService).sendEmail(any(EmailEntity.class));

    assertThrows(
        RuntimeException.class,
        () -> {
          sendEmailToQueueUseCase.sendEmail(DEFAULT_EMAIL_VO);
        });

    verify(checkIdentificationNumberService, times(1))
        .isValid(DEFAULT_EMAIL_VO.getIdentificationNumber());
    verify(sendEmailService, times(1)).sendEmail(any(EmailEntity.class));
  }
}
