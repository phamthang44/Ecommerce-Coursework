package com.greenwich.ecommerce.infra.email;

import com.greenwich.ecommerce.dto.response.EmailConfirmResponse;
import com.greenwich.ecommerce.dto.response.ResponseData;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendRegistrationEmail(String toEmail,
                                String subject,
                                String body) {


        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("${spring.mail.username}");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendEmailRegistrationHtml(String toEmail, String username) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("Welcome to CheapDeals.com LTD! Please Confirm Your Email.");

        String htmlContent = String.format(
                """
                <html>
                <body>
                    <p>Welcome %s to CheapDeals.com LTD!</p>
                    <p>Please click the button below to confirm:</p>
                    <a href="http://localhost:3000/api/v1/email/confirm?token=123456"
                       style="display: inline-block; padding: 10px 20px; background-color: #4CAF50;
                              color: white; text-decoration: none; border-radius: 5px;">
                        Account Confirmation
                    </a>
                </body>
                </html>
                """, username
        );

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public EmailConfirmResponse confirmToken(String token) {
        if (token.equals("123456")) {
            return new EmailConfirmResponse("Token is valid!", token);
        }
        return null;
    }
}