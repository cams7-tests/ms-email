package br.cams7.tests.ms.infra.entrypoint;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import br.cams7.tests.ms.core.port.in.SendEmailToQueueUseCase;
import br.cams7.tests.ms.infra.entrypoint.mapper.ResponseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SendEmailController {

  private final SendEmailDirectlyUseCase sendEmailDirectlyUseCase;
  private final SendEmailToQueueUseCase sendEmailToQueueUseCase;
  private final ResponseConverter responseConverter;

  @PostMapping(path = "/send-email-directly")
  @ResponseStatus(HttpStatus.OK)
  Mono<EmailResponseDTO> sendEmailDirectly(
      @RequestBody final SendEmailRequestDTO sendEmailRequest) {
    return sendEmailDirectlyUseCase
        .execute(getEmail(sendEmailRequest))
        .map(email -> responseConverter.convert(email));
  }

  @PostMapping(path = "/send-email-to-queue")
  @ResponseStatus(HttpStatus.OK)
  Mono<Void> sendEmailToQueue(@RequestBody final SendEmailRequestDTO sendEmailRequest) {
    return sendEmailToQueueUseCase.execute(getEmail(sendEmailRequest));
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
