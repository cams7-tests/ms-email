package br.cams7.tests.ms.infra.controllers;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.domain.PageInfo;
import br.cams7.tests.ms.core.ports.in.GetAllEmailsUseCase;
import br.cams7.tests.ms.core.ports.in.GetEmailUseCase;
import br.cams7.tests.ms.core.ports.in.SendEmailUseCase;
import br.cams7.tests.ms.infra.dtos.EmailDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController {

  private final SendEmailUseCase sendEmailUseCase;
  private final GetAllEmailsUseCase getAllEmailsUseCase;
  private final GetEmailUseCase getEmailUseCase;
  private final ModelMapper modelMapper;

  @Autowired
  EmailController(
      SendEmailUseCase sendEmailUseCase,
      GetAllEmailsUseCase getAllEmailsUseCase,
      GetEmailUseCase getEmailUseCase,
      ModelMapper modelMapper) {
    super();
    this.sendEmailUseCase = sendEmailUseCase;
    this.getAllEmailsUseCase = getAllEmailsUseCase;
    this.getEmailUseCase = getEmailUseCase;
    this.modelMapper = modelMapper;
  }

  @PostMapping("/sending-email")
  ResponseEntity<EmailEntity> sendingEmail(@RequestBody @Valid final EmailDto emailDto) {
    return new ResponseEntity<>(sendEmailUseCase.sendEmail(getEmail(emailDto)), HttpStatus.CREATED);
  }

  @GetMapping("/emails")
  ResponseEntity<Page<EmailEntity>> getAllEmails(
      @PageableDefault(page = 0, size = 5, sort = "emailId", direction = Sort.Direction.DESC)
          Pageable pageable) {
    List<EmailEntity> emailList = getAllEmailsUseCase.findAll(getPage(pageable));
    return new ResponseEntity<>(
        new PageImpl<EmailEntity>(emailList, pageable, emailList.size()), HttpStatus.OK);
  }

  @GetMapping("/emails/{emailId}")
  ResponseEntity<Object> getOneEmail(@PathVariable(value = "emailId") final UUID emailId) {
    Optional<EmailEntity> emailModelOptional = getEmailUseCase.findById(emailId);
    if (!emailModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found.");
    } else {
      return ResponseEntity.status(HttpStatus.OK).body(emailModelOptional.get());
    }
  }

  private EmailEntity getEmail(EmailDto emailDto) {
    return modelMapper.map(emailDto, EmailEntity.class);
  }

  private PageInfo getPage(Pageable pageable) {
    return modelMapper.map(pageable, PageInfo.class);
  }
}
