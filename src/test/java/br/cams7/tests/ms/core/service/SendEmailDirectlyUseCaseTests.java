package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.port.in.EmailVOTestData.defaultEmailVO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.defaultEmailEntity;
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
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import br.cams7.tests.ms.core.port.in.exception.InvalidIdentificationNumberException;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberService;
import br.cams7.tests.ms.core.port.out.SaveEmailRepository;
import br.cams7.tests.ms.core.port.out.SendEmailService;
import br.cams7.tests.ms.core.port.out.exception.SendEmailException;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;

class SendEmailDirectlyUseCaseTests {

  private static final EmailVO DEFAULT_EMAIL_VO = defaultEmailVO();
  private static final boolean IS_VALID_IDENTIFICATION_NUMBER = true;
  private static final boolean IS_INVALID_IDENTIFICATION_NUMBER = false;
  private static final EmailEntity DEFAULT_EMAIL_ENTITY = defaultEmailEntity();
  private static final ArgumentCaptor<EmailEntity> EMAIL_ENTITY_CAPTOR =
      ArgumentCaptor.forClass(EmailEntity.class);
  private static final String ERROR_MESSAGE = "Error";

  private static final ModelMapper MODEL_MAPPER = new ModelMapper();
  private final SaveEmailRepository saveEmailRepository = mock(SaveEmailRepository.class);
  private final SendEmailService sendEmailService = mock(SendEmailService.class);
  private final CheckIdentificationNumberService checkIdentificationNumberService =
      mock(CheckIdentificationNumberService.class);

  private final SendEmailDirectlyUseCase sendEmailDirectlyUseCase =
      new SendEmailDirectlyUseCaseImpl(
          MODEL_MAPPER, saveEmailRepository, sendEmailService, checkIdentificationNumberService);

  @BeforeEach
  void setUp() throws SendEmailException {
    given(checkIdentificationNumberService.isValid(anyString()))
        .willReturn(IS_VALID_IDENTIFICATION_NUMBER);
    willDoNothing().given(sendEmailService).sendEmail(any(EmailEntity.class));
    given(saveEmailRepository.save(any(EmailEntity.class))).willReturn(DEFAULT_EMAIL_ENTITY);
  }

  @Test
  @DisplayName("sendEmail returns email whith sent status when successfull")
  void sendEmail_ReturnsEmailWithSentStatus_WhenSuccessful() throws SendEmailException {
    var email = sendEmailDirectlyUseCase.sendEmail(DEFAULT_EMAIL_VO);

    assertThat(email).isEqualTo(DEFAULT_EMAIL_ENTITY);

    then(checkIdentificationNumberService)
        .should(times(1))
        .isValid(eq(DEFAULT_EMAIL_VO.getIdentificationNumber()));

    then(sendEmailService).should(times(1)).sendEmail(EMAIL_ENTITY_CAPTOR.capture());
    assertThat(
            EMAIL_ENTITY_CAPTOR
                .getValue()
                .withEmailSentDate(DEFAULT_EMAIL_ENTITY.getEmailSentDate()))
        .isEqualTo(DEFAULT_EMAIL_ENTITY.withEmailId(null));

    then(saveEmailRepository).should(times(1)).save(EMAIL_ENTITY_CAPTOR.capture());
    assertThat(
            EMAIL_ENTITY_CAPTOR
                .getValue()
                .withEmailSentDate(DEFAULT_EMAIL_ENTITY.getEmailSentDate()))
        .isEqualTo(DEFAULT_EMAIL_ENTITY.withEmailId(null));
  }

  @Test
  @DisplayName("sendEmail throws error when pass null email vo")
  void sendEmail_ThrowsError_WhenPassNullEmailVo() throws SendEmailException {
    assertThrows(
        NullPointerException.class,
        () -> {
          sendEmailDirectlyUseCase.sendEmail(null);
        });

    then(checkIdentificationNumberService).should(times(0)).isValid(anyString());
    then(sendEmailService).should(times(0)).sendEmail(any(EmailEntity.class));
    then(saveEmailRepository).should(times(0)).save(any(EmailEntity.class));
  }

  @Test
  @DisplayName("sendEmail throws error when invalid identification number")
  void sendEmail_ThrowsError_WhenInvalidIdentificationNumber() throws SendEmailException {
    given(checkIdentificationNumberService.isValid(anyString()))
        .willReturn(IS_INVALID_IDENTIFICATION_NUMBER);

    var thrown =
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

    then(checkIdentificationNumberService)
        .should(times(1))
        .isValid(eq(DEFAULT_EMAIL_VO.getIdentificationNumber()));
    then(sendEmailService).should(times(0)).sendEmail(any(EmailEntity.class));
    then(saveEmailRepository).should(times(0)).save(any(EmailEntity.class));
  }

  @Test
  @DisplayName(
      "sendEmail returns email with error status when some error happened during send email")
  void sendEmail_ReturnsEmailWhithErrorStatus_WhenSomeErrorHappenedDuringSendEmail()
      throws SendEmailException {

    var defaultEmail = DEFAULT_EMAIL_ENTITY.withEmailStatus(EmailStatusEnum.ERROR);

    willThrow(new SendEmailException(ERROR_MESSAGE, null))
        .given(sendEmailService)
        .sendEmail(any(EmailEntity.class));
    given(saveEmailRepository.save(any(EmailEntity.class))).willReturn(defaultEmail);

    var email = sendEmailDirectlyUseCase.sendEmail(DEFAULT_EMAIL_VO);

    assertThat(email).isEqualTo(defaultEmail);

    then(checkIdentificationNumberService)
        .should(times(1))
        .isValid(eq(DEFAULT_EMAIL_VO.getIdentificationNumber()));

    then(sendEmailService).should(times(1)).sendEmail(EMAIL_ENTITY_CAPTOR.capture());
    assertThat(
            EMAIL_ENTITY_CAPTOR
                .getValue()
                .withEmailSentDate(DEFAULT_EMAIL_ENTITY.getEmailSentDate()))
        .isEqualTo(DEFAULT_EMAIL_ENTITY.withEmailId(null).withEmailStatus(EmailStatusEnum.ERROR));

    then(saveEmailRepository).should(times(1)).save(EMAIL_ENTITY_CAPTOR.capture());
    assertThat(
            EMAIL_ENTITY_CAPTOR
                .getValue()
                .withEmailSentDate(DEFAULT_EMAIL_ENTITY.getEmailSentDate()))
        .isEqualTo(DEFAULT_EMAIL_ENTITY.withEmailId(null).withEmailStatus(EmailStatusEnum.ERROR));
  }
}
