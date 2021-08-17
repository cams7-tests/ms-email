package com.ms.email.application.services;

import com.ms.email.application.domain.Email;
import com.ms.email.application.domain.enums.StatusEmail;
import com.ms.email.application.ports.EmailRepository;
import com.ms.email.application.ports.EmailService;
import com.ms.email.application.ports.SendEmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final SendEmailService sendEmailService;

    public EmailServiceImpl(final EmailRepository emailRepository, final SendEmailService sendEmailService) {
        this.emailRepository = emailRepository;
        this.sendEmailService = sendEmailService;
    }

    @Override
    public Email sendEmail(Email email) {
        email.setSendDateEmail(LocalDateTime.now());
        try{
            sendEmailService.sendEmailSmtp(email);
            email.setStatusEmail(StatusEmail.SENT);
        } catch (Exception e){
            email.setStatusEmail(StatusEmail.ERROR);
        } finally {
            return save(email);
        }
    }

    @Override
    public Page<Email> findAll(Pageable pageable) {
        //inserir manipulação de dados/regras
        return  emailRepository.findAll(pageable);
    }

    @Override
    public Optional<Email> findById(UUID emailId) {
        //inserir manipulação de dados/regras
        return emailRepository.findById(emailId);
    }

    @Override
    public Email save(Email email) {
        return emailRepository.save(email);
    }
}
