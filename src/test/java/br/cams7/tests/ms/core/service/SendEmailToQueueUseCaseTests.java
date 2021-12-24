package br.cams7.tests.ms.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.EmailVOTestData;
import br.cams7.tests.ms.core.port.in.SendEmailToQueueUseCase;
import br.cams7.tests.ms.core.port.in.exception.InvalidIdentificationNumberException;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberService;
import br.cams7.tests.ms.core.port.out.SendEmailToQueueService;
import br.cams7.tests.ms.core.port.out.exception.SendEmailException;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailEntityTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

class SendEmailToQueueUseCaseTests {

  private static final EmailVO DEFAULT_EMAIL_VO = EmailVOTestData.defaultEmail();
  private static final boolean IS_VALID_IDENTIFICATION_NUMBER = true;
  private static final boolean IS_INVALID_IDENTIFICATION_NUMBER = false;
  private static final EmailEntity DEFAULT_EMAIL_ENTITY = EmailEntityTestData.defaultEmail();

  private static final ModelMapper MODEL_MAPPER = new ModelMapper();
  private final SendEmailToQueueService sendEmailService = mock(SendEmailToQueueService.class);
  private final CheckIdentificationNumberService checkIdentificationNumberService =
      mock(CheckIdentificationNumberService.class);

  private final SendEmailToQueueUseCase sendEmailToQueueUseCase =
      new SendEmailToQueueUseCaseImpl(
          MODEL_MAPPER, sendEmailService, checkIdentificationNumberService);

  @BeforeEach
  void setUp() throws SendEmailException {
    given(checkIdentificationNumberService.isValid(anyString()))
        .willReturn(IS_VALID_IDENTIFICATION_NUMBER);
    willDoNothing().given(sendEmailService).sendEmail(any(EmailEntity.class));
  }

  @Test
  @DisplayName("sendEmail when successfull")
  void sendEmail_WhenSuccessful() {
    var newEmail =
        DEFAULT_EMAIL_ENTITY.withEmailId(null).withEmailSentDate(null).withEmailStatus(null);
    sendEmailToQueueUseCase.sendEmail(DEFAULT_EMAIL_VO);

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

    then(checkIdentificationNumberService).should(times(0)).isValid(anyString());
    then(sendEmailService).should(times(0)).sendEmail(any(EmailEntity.class));
  }

  @Test
  @DisplayName("sendEmail throws error when invalid identification number")
  void sendEmail_ThrowsError_WhenInvalidIdentificationNumber() {
    given(checkIdentificationNumberService.isValid(anyString()))
        .willReturn(IS_INVALID_IDENTIFICATION_NUMBER);

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

    then(checkIdentificationNumberService)
        .should(times(1))
        .isValid(eq(DEFAULT_EMAIL_VO.getIdentificationNumber()));
    then(sendEmailService).should(times(0)).sendEmail(any(EmailEntity.class));
  }

  @Test
  @DisplayName("sendEmail throws error when some error happened during send email")
  void sendEmail_ThrowsError_WhenSomeErrorHappenedDuringSendEmail() {
    var newEmail =
        DEFAULT_EMAIL_ENTITY.withEmailId(null).withEmailSentDate(null).withEmailStatus(null);
    willThrow(new RuntimeException()).given(sendEmailService).sendEmail(any(EmailEntity.class));

    assertThrows(
        RuntimeException.class,
        () -> {
          sendEmailToQueueUseCase.sendEmail(DEFAULT_EMAIL_VO);
        });

    then(checkIdentificationNumberService)
        .should(times(1))
        .isValid(eq(DEFAULT_EMAIL_VO.getIdentificationNumber()));
    then(sendEmailService).should(times(1)).sendEmail(eq(newEmail));
  }
}
