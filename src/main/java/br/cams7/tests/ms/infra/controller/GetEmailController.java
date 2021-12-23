package br.cams7.tests.ms.infra.controller;

import br.cams7.tests.ms.core.common.PageDTO;
import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.in.GetEmailsUseCase;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetEmailController {

  private final GetEmailsUseCase getAllEmailsUseCase;
  private final GetEmailUseCase getEmailUseCase;
  private final ModelMapper modelMapper;

  @Autowired
  GetEmailController(
      GetEmailsUseCase getAllEmailsUseCase,
      GetEmailUseCase getEmailUseCase,
      ModelMapper modelMapper) {
    super();
    this.getAllEmailsUseCase = getAllEmailsUseCase;
    this.getEmailUseCase = getEmailUseCase;
    this.modelMapper = modelMapper;
  }

  @GetMapping(path = "/emails")
  ResponseEntity<Page<EmailEntity>> getAllEmails(
      @PageableDefault(page = 0, size = 5, sort = "emailId", direction = Sort.Direction.DESC)
          Pageable pageable) {
    List<EmailEntity> emailList = getAllEmailsUseCase.findAll(getPage(pageable));
    return new ResponseEntity<>(
        new PageImpl<>(emailList, pageable, emailList.size()), HttpStatus.OK);
  }

  @GetMapping(path = "/emails/{emailId}")
  ResponseEntity<Object> getOneEmail(@PathVariable(value = "emailId") final UUID emailId) {
    Optional<EmailEntity> emailModelOptional = getEmailUseCase.findById(emailId);
    if (!emailModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found.");
    } else {
      return ResponseEntity.status(HttpStatus.OK).body(emailModelOptional.get());
    }
  }

  private PageDTO getPage(Pageable pageable) {
    return modelMapper.map(pageable, PageDTO.class);
  }
}
