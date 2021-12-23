package br.cams7.tests.ms.infra.controller;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import br.cams7.tests.ms.core.port.in.SendEmailToQueueUseCase;
import br.cams7.tests.ms.domain.EmailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendEmailController {

  private final SendEmailDirectlyUseCase sendEmailDirectlyUseCase;
  private final SendEmailToQueueUseCase sendEmailToQueueUseCase;

  @Autowired
  SendEmailController(
      SendEmailDirectlyUseCase sendEmailDirectlyUseCase,
      SendEmailToQueueUseCase sendEmailToQueueUseCase) {
    super();
    this.sendEmailDirectlyUseCase = sendEmailDirectlyUseCase;
    this.sendEmailToQueueUseCase = sendEmailToQueueUseCase;
  }

  @PostMapping(path = "/send-email-directly")
  ResponseEntity<EmailEntity> sendEmailDirectly(
      @RequestBody final SendEmailRequestDTO sendEmailRequest) {
    return new ResponseEntity<>(
        sendEmailDirectlyUseCase.sendEmail(getEmail(sendEmailRequest)), HttpStatus.OK);
  }

  @PostMapping(path = "/send-email-to-queue")
  ResponseEntity<Void> sendEmailToQueue(@RequestBody final SendEmailRequestDTO sendEmailRequest) {
    sendEmailToQueueUseCase.sendEmail(getEmail(sendEmailRequest));
    return new ResponseEntity<>(HttpStatus.OK);
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
