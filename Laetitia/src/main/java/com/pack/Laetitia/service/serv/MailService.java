package com.pack.Laetitia.service.serv;

import com.pack.Laetitia.packManager.exceptio.ApiException;
import com.pack.Laetitia.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.pack.Laetitia.packManager.util.EmailUtils.getEmailMessage;
import static com.pack.Laetitia.packManager.util.EmailUtils.getResetPasswordMessage;

@Slf4j
@Service
@RequiredArgsConstructor
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MailService implements EmailService {

    public static final String NEW_ACCOUNT_EMAIL_VERIFICATION = "New Account Email verification";
    public static final String PASSWORD_RESET_REQUEST = "Password reset request";
    private final JavaMailSender mailSender;

    @Value("${VERIFY_EMAIL_HOST}")
    private String host;
    @Value("${EMAIL_ID}")
    private String fromEmail;


    @Override
    @Async
    public void sendNewAccountEmail(String name, String to, String token) {

        try{
            var message = new SimpleMailMessage();
            message.setSubject(NEW_ACCOUNT_EMAIL_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getEmailMessage(name, host, token));

            mailSender.send(message);
        }catch(Exception e){

            e.printStackTrace();
            throw new ApiException("Unable to send Email message!!");
        }
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String name, String to, String token) {

        try{
            var message = new SimpleMailMessage();
            message.setSubject(PASSWORD_RESET_REQUEST);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getResetPasswordMessage(name, host, token));

            mailSender.send(message);
        }catch(Exception e){

            e.printStackTrace();
            throw new ApiException("Unable to send Email message!!");
        }
    }
}
