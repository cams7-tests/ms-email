package br.cams7.tests.ms.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.EmailVOTestData;
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import br.cams7.tests.ms.core.port.in.exception.InvalidIdentificationNumberException;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberService;
import br.cams7.tests.ms.core.port.out.EmailRepository;
import br.cams7.tests.ms.core.port.out.SendEmailService;
import br.cams7.tests.ms.core.port.out.exception.SendEmailException;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailEntityTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SendEmailDirectlyUseCaseTests {

  private static final EmailVO DEFAULT_EMAIL_VO = EmailVOTestData.defaultEmail();
  private static final boolean IS_VALID_IDENTIFICATION_NUMBER = true;
  private static final boolean IS_INVALID_IDENTIFICATION_NUMBER = false;
  private static final EmailEntity DEFAULT_EMAIL_ENTITY = EmailEntityTestData.defaultEmail();

  private final EmailRepository emailRepository = Mockito.mock(EmailRepository.class);
  private final SendEmailService sendEmailService = Mockito.mock(SendEmailService.class);
  private final CheckIdentificationNumberService checkIdentificationNumberService =
      Mockito.mock(CheckIdentificationNumberService.class);

  private final SendEmailDirectlyUseCase sendEmailDirectlyUseCase =
      new SendEmailDirectlyUseCaseImpl(
          emailRepository, sendEmailService, checkIdentificationNumberService);

  @BeforeEach
  void setUp() throws SendEmailException {
    given(checkIdentificationNumberService.isValid(DEFAULT_EMAIL_VO.getIdentificationNumber()))
        .willReturn(IS_VALID_IDENTIFICATION_NUMBER);
    doNothing().when(sendEmailService).sendEmail(any(EmailEntity.class));
    given(emailRepository.save(any(EmailEntity.class))).willReturn(DEFAULT_EMAIL_ENTITY);
  }

  @Test
  @DisplayName("sendEmail returns sent email when successfull")
  void sendEmail_ReturnsSentEmail_WhenSuccessful() throws SendEmailException {
    var email = sendEmailDirectlyUseCase.sendEmail(DEFAULT_EMAIL_VO);

    assertThat(email).isNotNull();
    assertThat(email).isEqualTo(DEFAULT_EMAIL_ENTITY);

    verify(checkIdentificationNumberService, times(1))
        .isValid(DEFAULT_EMAIL_VO.getIdentificationNumber());
    verify(sendEmailService, times(1)).sendEmail(any(EmailEntity.class));
    verify(emailRepository, times(1)).save(any(EmailEntity.class));
  }

  @Test
  @DisplayName("sendEmail throws error when pass null email vo")
  void sendEmail_ThrowsError_WhenPassNullEmailVo() throws SendEmailException {

    assertThrows(
        NullPointerException.class,
        () -> {
          sendEmailDirectlyUseCase.sendEmail(null);
        });

    verify(checkIdentificationNumberService, times(0))
        .isValid(DEFAULT_EMAIL_VO.getIdentificationNumber());
    verify(sendEmailService, times(0)).sendEmail(any(EmailEntity.class));
    verify(emailRepository, times(0)).save(any(EmailEntity.class));
  }

  @Test
  @DisplayName("sendEmail throws error when invalid identification number")
  void sendEmail_ThrowsError_WhenInvalidIdentificationNumber() throws SendEmailException {
    when(checkIdentificationNumberService.isValid(DEFAULT_EMAIL_VO.getIdentificationNumber()))
        .thenReturn(IS_INVALID_IDENTIFICATION_NUMBER);

    InvalidIdentificationNumberException thrown =
        assertThrows(
            InvalidIdentificationNumberException.class,
            () -> {
              sendEmailDirectlyUseCase.sendEmail(DEFAULT_EMAIL_VO);
            });

    assertThat(thrown.getMessage())
        .isEqualTo(
            String.format(
                "The identification number \"%s\" isn't valid",
                DEFAULT_EMAIL_VO.getIdentificationNumber()));

    verify(checkIdentificationNumberService, times(1))
        .isValid(DEFAULT_EMAIL_VO.getIdentificationNumber());
    verify(sendEmailService, times(0)).sendEmail(any(EmailEntity.class));
    verify(emailRepository, times(0)).save(any(EmailEntity.class));
  }
}
