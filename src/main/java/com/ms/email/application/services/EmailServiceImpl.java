package com.ms.email.application.services;

import com.ms.email.application.entities.EmailModel;
import com.ms.email.application.entities.enums.StatusEmail;
import com.ms.email.application.ports.EmailRepository;
import com.ms.email.application.ports.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;

    private final JavaMailSender emailSender;

    public EmailServiceImpl(final EmailRepository emailRepository, final JavaMailSender emailSender) {
        this.emailRepository = emailRepository;
        this.emailSender = emailSender;
    }

    @Override
    public EmailModel sendEmail(EmailModel emailModel) {
        emailModel.setSendDateEmail(LocalDateTime.now());
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailModel.getEmailFrom());
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());
            emailSender.send(message);

            emailModel.setStatusEmail(StatusEmail.SENT);
        } catch (MailException e){
            emailModel.setStatusEmail(StatusEmail.ERROR);
        } finally {
            return emailRepository.save(emailModel);
        }
    }

    @Override
    public Page<EmailModel> findAll(Pageable pageable) {
        //inserir manipulação de dados/regras
        return  emailRepository.findAll(pageable);
    }

    @Override
    public Optional<EmailModel> findById(UUID emailId) {
        //inserir manipulação de dados/regras
        return emailRepository.findById(emailId);
    }
}
