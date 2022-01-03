package br.cams7.tests.ms.infra.client;

import static br.cams7.tests.ms.domain.EmailEntityTestData.OWNER_REF;
import static br.cams7.tests.ms.infra.client.response.CheckIdentificationNumberResponseTestData.aprovedCheckIdentificationNumberResponse;
import static br.cams7.tests.ms.infra.client.response.CheckIdentificationNumberResponseTestData.notAprovedCheckIdentificationNumberResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;

import br.cams7.tests.ms.infra.client.response.CheckIdentificationNumberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckIdentificationNumberServiceImplTests {

  private static final CheckIdentificationNumberResponse
      APROVED_CHECK_IDENTIFICATION_NUMBER_RESPONSE = aprovedCheckIdentificationNumberResponse();
  private static final CheckIdentificationNumberResponse
      NOT_APROVED_CHECK_IDENTIFICATION_NUMBER_RESPONSE =
          notAprovedCheckIdentificationNumberResponse();
  private static final String IDENTIFICATION_NUMBER = OWNER_REF;

  @InjectMocks private CheckIdentificationNumberServiceImpl checkIdentificationNumberService;

  @Mock private CheckIdentificationNumberClient client;

  @Test
  @DisplayName("isValid when aproved")
  void isValid_WhenAproved() {

    given(client.queryStatus(anyString())).willReturn(APROVED_CHECK_IDENTIFICATION_NUMBER_RESPONSE);

    var valid = checkIdentificationNumberService.isValid(IDENTIFICATION_NUMBER);

    assertThat(valid).isTrue();

    then(client).should().queryStatus(eq(IDENTIFICATION_NUMBER));
  }

  @Test
  @DisplayName("isValid when not aproved")
  void isValid_WhenNotAproved() {

    given(client.queryStatus(anyString()))
        .willReturn(NOT_APROVED_CHECK_IDENTIFICATION_NUMBER_RESPONSE);

    var valid = checkIdentificationNumberService.isValid(IDENTIFICATION_NUMBER);

    assertThat(valid).isFalse();

    then(client).should().queryStatus(eq(IDENTIFICATION_NUMBER));
  }

  @Test
  @DisplayName("isValid throws error when pass null identification number")
  void isValid_ThrowsError_WhenPassNullIdentificationNumber() {
    assertThrows(
        NullPointerException.class,
        () -> {
          checkIdentificationNumberService.isValid(null);
        });

    then(client).should(never()).queryStatus(anyString());
  }

  @Test
  @DisplayName(
      "isValid throws error when some error happened during validation of identification number")
  void isValid_ThrowsError_WhenSomeErrorHappenedDuringValidationOfIdenficationNumber() {
    willThrow(RuntimeException.class).given(client).queryStatus(anyString());

    assertThrows(
        RuntimeException.class,
        () -> {
          checkIdentificationNumberService.isValid(IDENTIFICATION_NUMBER);
        });

    then(client).should().queryStatus(eq(IDENTIFICATION_NUMBER));
  }
}
