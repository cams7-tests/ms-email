package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.port.in.EmailVOTestData.defaultEmailVO;
import static br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTOTestData.defaultEmailResponseDTO;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.exception.InvalidIdentificationNumberException;
import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberService;
import br.cams7.tests.ms.core.port.out.SaveEmailRepository;
import br.cams7.tests.ms.core.port.out.SendEmailService;
import br.cams7.tests.ms.core.port.out.exception.SendEmailException;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailStatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class SendEmailDirectlyUseCaseImplTests {

  private static final EmailVO DEFAULT_EMAIL_VO = defaultEmailVO();
  private static final boolean IS_VALID_IDENTIFICATION_NUMBER = true;
  private static final boolean IS_INVALID_IDENTIFICATION_NUMBER = false;
  private static final EmailEntity DEFAULT_EMAIL_ENTITY = defaultEmailEntity();
  private static final EmailResponseDTO DEFAULT_EMAIL_RESPONSE_DTO = defaultEmailResponseDTO();
  private static final String ERROR_MESSAGE = "Error";

  @InjectMocks private SendEmailDirectlyUseCaseImpl sendEmailDirectlyUseCase;

  @Spy private ModelMapper modelMapper = new ModelMapper();
  @Mock private SaveEmailRepository saveEmailRepository;
  @Mock private SendEmailService sendEmailService;
  @Mock private CheckIdentificationNumberService checkIdentificationNumberService;

  @Captor private ArgumentCaptor<EmailEntity> emailEntityCaptor;

  @Test
  @DisplayName("sendEmail returns email with sent status when successfull")
  void sendEmail_ReturnsEmailWithSentStatus_WhenSuccessful() throws SendEmailException {
    given(checkIdentificationNumberService.isValid(anyString()))
        .willReturn(IS_VALID_IDENTIFICATION_NUMBER);
    willDoNothing().given(sendEmailService).sendEmail(any(EmailEntity.class));
    given(saveEmailRepository.save(any(EmailEntity.class))).willReturn(DEFAULT_EMAIL_ENTITY);

    var email = sendEmailDirectlyUseCase.sendEmail(DEFAULT_EMAIL_VO);

    assertThat(email).isEqualTo(DEFAULT_EMAIL_RESPONSE_DTO);

    then(checkIdentificationNumberService)
        .should(times(1))
        .isValid(eq(DEFAULT_EMAIL_VO.getIdentificationNumber()));

    then(sendEmailService).should(times(1)).sendEmail(emailEntityCaptor.capture());
    assertThat(
            emailEntityCaptor.getValue().withEmailSentDate(DEFAULT_EMAIL_ENTITY.getEmailSentDate()))
        .isEqualTo(DEFAULT_EMAIL_ENTITY.withEmailId(null));

    then(saveEmailRepository).should(times(1)).save(emailEntityCaptor.capture());
    assertThat(
            emailEntityCaptor.getValue().withEmailSentDate(DEFAULT_EMAIL_ENTITY.getEmailSentDate()))
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

    then(checkIdentificationNumberService).should(never()).isValid(anyString());
    then(sendEmailService).should(never()).sendEmail(any(EmailEntity.class));
    then(saveEmailRepository).should(never()).save(any(EmailEntity.class));
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
    then(sendEmailService).should(never()).sendEmail(any(EmailEntity.class));
    then(saveEmailRepository).should(never()).save(any(EmailEntity.class));
  }

  @Test
  @DisplayName(
      "sendEmail returns email with error status when some error happened during send email")
  void sendEmail_ReturnsEmailWhithErrorStatus_WhenSomeErrorHappenedDuringSendEmail()
      throws SendEmailException {

    given(checkIdentificationNumberService.isValid(anyString()))
        .willReturn(IS_VALID_IDENTIFICATION_NUMBER);

    willThrow(new SendEmailException(ERROR_MESSAGE, null))
        .given(sendEmailService)
        .sendEmail(any(EmailEntity.class));
    given(saveEmailRepository.save(any(EmailEntity.class)))
        .willReturn(DEFAULT_EMAIL_ENTITY.withEmailStatus(EmailStatusEnum.ERROR));

    var email = sendEmailDirectlyUseCase.sendEmail(DEFAULT_EMAIL_VO);

    var response = DEFAULT_EMAIL_RESPONSE_DTO;
    response.setEmailStatus(EmailStatusEnum.ERROR);

    assertThat(email).isEqualTo(response);

    then(checkIdentificationNumberService)
        .should(times(1))
        .isValid(eq(DEFAULT_EMAIL_VO.getIdentificationNumber()));

    then(sendEmailService).should(times(1)).sendEmail(emailEntityCaptor.capture());
    assertThat(
            emailEntityCaptor.getValue().withEmailSentDate(DEFAULT_EMAIL_ENTITY.getEmailSentDate()))
        .isEqualTo(DEFAULT_EMAIL_ENTITY.withEmailId(null).withEmailStatus(EmailStatusEnum.ERROR));

    then(saveEmailRepository).should(times(1)).save(emailEntityCaptor.capture());
    assertThat(
            emailEntityCaptor.getValue().withEmailSentDate(DEFAULT_EMAIL_ENTITY.getEmailSentDate()))
        .isEqualTo(DEFAULT_EMAIL_ENTITY.withEmailId(null).withEmailStatus(EmailStatusEnum.ERROR));
  }
}
