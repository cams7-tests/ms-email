package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.port.in.EmailVOTestData.getEmailVO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.getEmailEntity;
import static br.cams7.tests.ms.domain.EmailStatusEnum.ERROR;
import static br.cams7.tests.ms.domain.EmailStatusEnum.SENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static reactor.test.StepVerifier.create;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.exception.InvalidIdentificationNumberException;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberGateway;
import br.cams7.tests.ms.core.port.out.SaveEmailGateway;
import br.cams7.tests.ms.core.port.out.SendEmailGateway;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
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
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@ExtendWith(MockitoExtension.class)
class SendEmailDirectlyUseCaseImplTests {

  private static final EmailVO DEFAULT_EMAIL_VO = getEmailVO();
  private static final boolean IS_VALID_IDENTIFICATION_NUMBER = true;
  private static final boolean IS_INVALID_IDENTIFICATION_NUMBER = false;
  private static final EmailEntity DEFAULT_EMAIL_ENTITY = getEmailEntity();

  @InjectMocks private SendEmailDirectlyUseCaseImpl sendEmailDirectlyUseCase;

  @Spy private ModelMapper modelMapper = new ModelMapper();
  @Mock private SaveEmailGateway saveEmailGateway;
  @Mock private SendEmailGateway sendEmailGateway;
  @Mock private CheckIdentificationNumberGateway checkIdentificationNumberGateway;

  @Captor private ArgumentCaptor<EmailEntity> emailEntityCaptor;

  @BeforeAll
  static void blockHoundSetup() {
    BlockHound.install();
  }

  @Test
  void blockHoundWorks() {
    try {
      FutureTask<?> task =
          new FutureTask<>(
              () -> {
                Thread.sleep(0); // NOSONAR
                return "";
              });
      Schedulers.parallel().schedule(task);

      task.get(10, TimeUnit.SECONDS);
      fail("should fail");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof BlockingOperationError);
    }
  }

  @Test
  @DisplayName("sendEmail returns email with sent status when successfull")
  void sendEmail_ReturnsEmailWithSentStatus_WhenSuccessful() {
    given(checkIdentificationNumberGateway.isValid(anyString()))
        .willReturn(Mono.just(IS_VALID_IDENTIFICATION_NUMBER));
    given(sendEmailGateway.sendEmail(any(EmailEntity.class))).willReturn(Mono.just(SENT));
    given(saveEmailGateway.save(any(EmailEntity.class)))
        .willReturn(Mono.just(DEFAULT_EMAIL_ENTITY));

    create(sendEmailDirectlyUseCase.execute(DEFAULT_EMAIL_VO))
        .expectSubscription()
        .expectNext(DEFAULT_EMAIL_ENTITY)
        .verifyComplete();

    then(checkIdentificationNumberGateway)
        .should(times(1))
        .isValid(eq(DEFAULT_EMAIL_VO.getIdentificationNumber()));

    then(sendEmailGateway).should(times(1)).sendEmail(emailEntityCaptor.capture());
    assertThat(
            emailEntityCaptor.getValue().withEmailSentDate(DEFAULT_EMAIL_ENTITY.getEmailSentDate()))
        .isEqualTo(DEFAULT_EMAIL_ENTITY.withEmailId(null));

    then(saveEmailGateway).should(times(1)).save(emailEntityCaptor.capture());
    assertThat(
            emailEntityCaptor.getValue().withEmailSentDate(DEFAULT_EMAIL_ENTITY.getEmailSentDate()))
        .isEqualTo(DEFAULT_EMAIL_ENTITY.withEmailId(null));
  }

  @Test
  @DisplayName("sendEmail throws error when pass null email vo")
  void sendEmail_ThrowsError_WhenPassNullEmailVo() {
    assertThrows(
        NullPointerException.class,
        () -> {
          sendEmailDirectlyUseCase.execute(null);
        });

    then(checkIdentificationNumberGateway).should(never()).isValid(anyString());
    then(sendEmailGateway).should(never()).sendEmail(any(EmailEntity.class));
    then(saveEmailGateway).should(never()).save(any(EmailEntity.class));
  }

  @Test
  @DisplayName("sendEmail returns error when invalid identification number")
  void sendEmail_ReturnsError_WhenInvalidIdentificationNumber() {
    given(checkIdentificationNumberGateway.isValid(anyString()))
        .willReturn(Mono.just(IS_INVALID_IDENTIFICATION_NUMBER));

    create(sendEmailDirectlyUseCase.execute(DEFAULT_EMAIL_VO))
        .expectSubscription()
        .expectError(InvalidIdentificationNumberException.class)
        .verify();

    then(checkIdentificationNumberGateway)
        .should(times(1))
        .isValid(eq(DEFAULT_EMAIL_VO.getIdentificationNumber()));
    then(sendEmailGateway).should(never()).sendEmail(any(EmailEntity.class));
    then(saveEmailGateway).should(never()).save(any(EmailEntity.class));
  }

  @Test
  @DisplayName(
      "sendEmail returns email with error status when some error happened during send email")
  void sendEmail_ReturnsEmailWhithErrorStatus_WhenSomeErrorHappenedDuringSendEmail() {
    given(checkIdentificationNumberGateway.isValid(anyString()))
        .willReturn(Mono.just(IS_VALID_IDENTIFICATION_NUMBER));

    given(sendEmailGateway.sendEmail(any(EmailEntity.class))).willReturn(Mono.just(ERROR));
    given(saveEmailGateway.save(any(EmailEntity.class)))
        .willReturn(Mono.just(DEFAULT_EMAIL_ENTITY.withEmailStatus(ERROR)));

    var response = DEFAULT_EMAIL_ENTITY;
    response.setEmailStatus(ERROR);

    create(sendEmailDirectlyUseCase.execute(DEFAULT_EMAIL_VO))
        .expectSubscription()
        .expectNext(response)
        .verifyComplete();

    then(checkIdentificationNumberGateway)
        .should(times(1))
        .isValid(eq(DEFAULT_EMAIL_VO.getIdentificationNumber()));

    then(sendEmailGateway).should(times(1)).sendEmail(emailEntityCaptor.capture());
    assertThat(
            emailEntityCaptor.getValue().withEmailSentDate(DEFAULT_EMAIL_ENTITY.getEmailSentDate()))
        .isEqualTo(DEFAULT_EMAIL_ENTITY.withEmailId(null).withEmailStatus(ERROR));

    then(saveEmailGateway).should(times(1)).save(emailEntityCaptor.capture());
    assertThat(
            emailEntityCaptor.getValue().withEmailSentDate(DEFAULT_EMAIL_ENTITY.getEmailSentDate()))
        .isEqualTo(DEFAULT_EMAIL_ENTITY.withEmailId(null).withEmailStatus(ERROR));
  }
}
