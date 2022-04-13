package com.en.remembrance.services;

import com.en.remembrance.dtos.EmailLinkDto;
import com.en.remembrance.dtos.ShareStoryEmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;


@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Async
    public Future<Boolean> emailSharedStory(ShareStoryEmailDto dto) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("senderName", dto.getSenderName());
        variables.put("url", dto.getUrl());
        //		variables.put("expiryDays", dto.getExpiryDays());

        //        String body = "Hello World";
        //        Map<String, InputStreamSource> attachments = Collections.singletonMap("logo", new ClassPathResource("logo.png"));
        Map<String, ClassPathResource> inlineResource = Collections.singletonMap("logo", new ClassPathResource("logo.png"));

        String template = "email-share-story";
        String subject = "enRemembrance || New Story";
        boolean isHtml = true;

        this.sendEmail(dto.getReceiverEmail(), senderEmail, subject, null, variables, null, inlineResource, template, isHtml);
        //        return true;
        return new AsyncResult<>(true);
    }

    @Async
    public Future<Boolean> emailNewRegistration(EmailLinkDto dto) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", dto.getName());
        variables.put("url", dto.getLink());
        variables.put("expiryDays", dto.getExpiryDays());

        Map<String, ClassPathResource> inlineResource = Collections.singletonMap("logo", new ClassPathResource("logo.png"));

        String template = "email-verification";
        String subject = "enRemembrance || Email verification";
        boolean isHtml = true;

        this.sendEmail(dto.getReceiverEmail(), senderEmail, subject, null, variables, null, inlineResource, template, isHtml);
        return new AsyncResult<>(true);
    }

    @Async
    public Future<Boolean> emailForgotPassword(EmailLinkDto dto) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", dto.getName());
        variables.put("url", dto.getLink());
        variables.put("expiryDays", dto.getExpiryDays());

        Map<String, ClassPathResource> inlineResource = Collections.singletonMap("logo", new ClassPathResource("logo.png"));

        String template = "email-forgot-password";
        String subject = "enRemembrance || Forgot Password";
        boolean isHtml = true;

        this.sendEmail(dto.getReceiverEmail(), senderEmail, subject, null, variables, null, inlineResource, template, isHtml);
        return new AsyncResult<>(true);
    }

    /**
     * @param to          sender email address
     * @param from        receiver email address
     * @param subject     of the email
     * @param body        will be sent if you want to send simple message without any template.
     *                    In this case you must send isHtml = false. Otherwise you can send null
     * @param variables   of html template. If you want to send email with html template
     *                    and this template has some variables, you can send it here.
     *                    Note: You have to send isHtml = true. Otherwise you can send null
     * @param attachments if there is any otherwise null
     * @param template    if you want to send email with html template otherwise null
     * @param isHtml      if true then html email will be sent. In this case @param template cannot
     *                    be null.
     * @throws MessagingException if there is any exception
     */
    private void sendEmail(String to, String from, String subject, String body, Map<String, Object> variables, Map<String, InputStreamSource> attachments, Map<String, ClassPathResource> inlineResource, String template, boolean isHtml) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        if (attachments != null) {
            for (Map.Entry<String, InputStreamSource> entry : attachments.entrySet()) {
                String key = entry.getKey();
                InputStreamSource value = entry.getValue();
                helper.addAttachment(key, value);
            }
        }

        if (isHtml) {
            Context context = new Context();
            context.setVariables(variables);
            String html = templateEngine.process(template, context);
            helper.setText(html, true);
        } else {
            helper.setText(body);
        }

        // Always add inline resource after helper.setText().
        // Else, mail readers might not be able to resolve inline references correctly.
        if (inlineResource != null) {
            for (Map.Entry<String, ClassPathResource> entry : inlineResource.entrySet()) {
                String key = entry.getKey();
                ClassPathResource value = entry.getValue();
                helper.addInline(key, value);
            }
        }

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(from);

        javaMailSender.send(message);
    }
}
