package br.cams7.tests.ms.infra.inbound.controllers;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.domain.PageInfo;
import br.cams7.tests.ms.core.ports.EmailServicePort;
import br.cams7.tests.ms.infra.dtos.EmailDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController {

  private final EmailServicePort emailServicePort;

  @Autowired
  EmailController(EmailServicePort emailServicePort) {
    super();
    this.emailServicePort = emailServicePort;
  }

  @PostMapping("/sending-email")
  ResponseEntity<EmailEntity> sendingEmail(@RequestBody @Valid final EmailDto emailDto) {
    EmailEntity email = new EmailEntity();
    BeanUtils.copyProperties(emailDto, email);
    return new ResponseEntity<>(emailServicePort.sendEmail(email), HttpStatus.CREATED);
  }

  @GetMapping("/emails")
  ResponseEntity<Page<EmailEntity>> getAllEmails(
      @PageableDefault(page = 0, size = 5, sort = "emailId", direction = Sort.Direction.DESC)
          Pageable pageable) {
    PageInfo pageInfo = new PageInfo();
    BeanUtils.copyProperties(pageable, pageInfo);
    List<EmailEntity> emailList = emailServicePort.findAll(pageInfo);
    return new ResponseEntity<>(
        new PageImpl<EmailEntity>(emailList, pageable, emailList.size()), HttpStatus.OK);
  }

  @GetMapping("/emails/{emailId}")
  ResponseEntity<Object> getOneEmail(@PathVariable(value = "emailId") final UUID emailId) {
    Optional<EmailEntity> emailModelOptional = emailServicePort.findById(emailId);
    if (!emailModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found.");
    } else {
      return ResponseEntity.status(HttpStatus.OK).body(emailModelOptional.get());
    }
  }
}
