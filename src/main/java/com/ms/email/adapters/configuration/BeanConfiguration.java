package com.ms.email.adapters.configuration;

import com.ms.email.EmailApplication;
import com.ms.email.application.ports.EmailRepository;
import com.ms.email.application.ports.SendEmailService;
import com.ms.email.application.services.EmailServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = EmailApplication.class)
public class BeanConfiguration {

    @Bean
    EmailServiceImpl emailServiceImpl(EmailRepository repository, SendEmailService sendEmailService) {
        return new EmailServiceImpl(repository, sendEmailService);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
