package com.en.remembrance.configurations;

import com.en.remembrance.properties.ApplicationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;


@Configuration
@EnableConfigurationProperties({ApplicationProperties.class})
public class ApplicationConfiguration {

//    @Bean
//    public JavaMailSenderImpl mailSender() {
//        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//
//        javaMailSender.setProtocol("smtp");
//        javaMailSender.setHost("smtp.gmail.com");
//        javaMailSender.setPort(465);
//        javaMailSender.setUsername("tekxma@gmail.com");
//        javaMailSender.setPassword("7uKtu!3e");
//
//        Properties p = new Properties();
//        p.setProperty("mail.smtp.auth", "true");
//        p.setProperty("mail.smtp.starttls.enable", "true");
//        p.setProperty("mail.smtp.starttls.required", "true");
//
//        javaMailSender.setJavaMailProperties(p);
//        return javaMailSender;
//    }
}