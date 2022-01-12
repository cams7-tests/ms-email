package br.cams7.tests.ms.infra.entrypoint;

import static org.springframework.http.HttpStatus.OK;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import br.cams7.tests.ms.core.port.in.SendEmailToQueueUseCase;
import br.cams7.tests.ms.infra.entrypoint.mapper.ResponseConverter;
import br.cams7.tests.ms.infra.entrypoint.request.SendEmailRequestDTO;
import br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SecurityScheme(
    name = SendEmailController.SECURITY_SCHEME_NAME,
    type = SecuritySchemeType.HTTP,
    scheme = SendEmailController.SECURITY_SCHEME_SCHEME)
@RestController
@RequiredArgsConstructor
public class SendEmailController {

  public static final String SECURITY_SCHEME_NAME = "Basic Authentication";
  public static final String SECURITY_SCHEME_SCHEME = "basic";
  public static final String OPERATION_TAGS = "ms-email";

  private final SendEmailDirectlyUseCase sendEmailDirectlyUseCase;
  private final SendEmailToQueueUseCase sendEmailToQueueUseCase;
  private final ResponseConverter responseConverter;

  @Operation(
      summary = "Send the email directly",
      tags = {OPERATION_TAGS},
      security = @SecurityRequirement(name = SECURITY_SCHEME_NAME))
  @PostMapping(path = "/send-email-directly")
  @ResponseStatus(OK)
  Mono<EmailResponseDTO> sendEmailDirectly(
      @RequestBody final SendEmailRequestDTO sendEmailRequest) {
    return sendEmailDirectlyUseCase
        .execute(getEmail(sendEmailRequest))
        .map(responseConverter::convert);
  }

  @Operation(
      summary = "Send the email indirectly",
      tags = {OPERATION_TAGS},
      security = @SecurityRequirement(name = SECURITY_SCHEME_NAME))
  @PostMapping(path = "/send-email-to-queue")
  @ResponseStatus(OK)
  Mono<Void> sendEmailToQueue(@RequestBody final SendEmailRequestDTO sendEmailRequest) {
    return sendEmailToQueueUseCase
        .execute(getEmail(sendEmailRequest))
        .flatMap(status -> Mono.empty());
  }

  private static EmailVO getEmail(SendEmailRequestDTO sendEmailRequest) {
    return new EmailVO(
        sendEmailRequest.getIdentificationNumber(),
        sendEmailRequest.getEmailFrom(),
        sendEmailRequest.getEmailTo(),
        sendEmailRequest.getSubject(),
        sendEmailRequest.getText());
  }
}
