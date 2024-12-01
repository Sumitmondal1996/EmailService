package com.scaler.emailservice.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.emailservice.dtos.Sendemaildto;
import com.scaler.emailservice.utils.EmailUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;



@Service
public class SendEmailbyEvent {
    private ObjectMapper objectMapper;
    public SendEmailbyEvent(ObjectMapper objectMapper)
    {
        this.objectMapper= objectMapper;
    }
    @KafkaListener(topics = "sendEmail", groupId = "emailservice")
    public void handleSendEmailbyEvent(String message) throws JsonProcessingException {
        Sendemaildto sendemaildto = objectMapper.readValue(
                message,
                Sendemaildto.class
        );
        String to = sendemaildto.getEmailid();
        String subject = sendemaildto.getSubject();
        String body = sendemaildto.getBody();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        // Create Authenticator
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Use environment variables or secure storage for sensitive information
                return new PasswordAuthentication(System.getenv("EMAIL_USERNAME"), "123"); // the password should be the app password
            }
        };

        // Create Mail Session
        Session session = Session.getInstance(props, auth);

        try {
            // Send Email
            EmailUtil.sendEmail(session, to, subject, body);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception (use proper logging in production)
        }



    }

}
