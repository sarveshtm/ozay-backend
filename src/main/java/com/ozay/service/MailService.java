package com.ozay.service;

import com.ozay.model.UserDetail;
import com.ozay.repository.UserDetailRepository;
import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Locale;

import com.sendgrid.*;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Inject
    private Environment env;

    @Inject
    private JavaMailSenderImpl javaMailSender;

    @Inject
    private MessageSource messageSource;

    @Inject
    private UserDetailRepository userDetailRepository;

    /**
     * System default email address that sends the e-mails.
     */
    private String from;

    private static final String EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @PostConstruct
    public void init() {
        this.from = env.getProperty("spring.mail.from");
    }

    public int sendGrid(String subject, String text, List<String> emailList){
        SendGrid sendgrid = new SendGrid("OzayOrg", "OzayOrg1124");

        int sentCount = 0;

        SendGrid.Email sendGrid = new SendGrid.Email();

        for(String memberEmail : emailList){
            if(memberEmail != null){
                if(memberEmail.matches(EMAIL_PATTERN)){
                    sendGrid.addSmtpApiTo(memberEmail);
                    sentCount++;
                }
            }
        }

        sendGrid.setFrom("noreply@ozay.us");
        sendGrid.setSubject(subject);
        sendGrid.setText(text);

        try {
            SendGrid.Response response = sendgrid.send(sendGrid);
            log.debug("Send email with sendgrid count {}", sentCount);
            System.out.println(response.getMessage());
        }
        catch (SendGridException e) {
            System.err.println(e);
        }
        return sentCount;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'!", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendActivationEmail(final String email, String content, Locale locale) {
        log.debug("Sending activation e-mail to '{}'", email);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(email, subject, content, false, true);
    }

    @Async
    public void sendActivationInvitationEmail(final String email, String content, Locale locale) {
        log.debug("Sending invitaion e-mail to '{}'", email);
        String subject = messageSource.getMessage("email.activation.invitation", null, locale);
        sendEmail(email, subject, content, false, true);
    }

    @Async
    public void sendActivationInvitationCompleteEmail(final String email, String content, Locale locale) {
        log.debug("Sending invitaion e-mail to '{}'", email);
        String subject = messageSource.getMessage("email.activation.invitation_registration_complete", null, locale);
        sendEmail(email, subject, content, false, true);
    }
}
