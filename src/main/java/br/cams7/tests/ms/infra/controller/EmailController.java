package br.cams7.tests.ms.infra.controller;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.GetAllEmailsUseCase;
import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.in.PageDTO;
import br.cams7.tests.ms.core.port.in.SendEmailUseCase;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
  ResponseEntity<EmailEntity> sendingEmail(@RequestBody final EmailRequestDTO dto) {
    return new ResponseEntity<>(sendEmailUseCase.sendEmail(getEmail(dto)), HttpStatus.CREATED);
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

  private static EmailVO getEmail(EmailRequestDTO dto) {
    EmailVO email =
        new EmailVO(
            dto.getOwnerRef(),
            dto.getEmailFrom(),
            dto.getEmailTo(),
            dto.getSubject(),
            dto.getText());
    return email;
  }

  private PageDTO getPage(Pageable pageable) {
    return modelMapper.map(pageable, PageDTO.class);
  }
}