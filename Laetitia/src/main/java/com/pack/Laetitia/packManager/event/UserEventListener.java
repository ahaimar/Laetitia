package com.pack.Laetitia.packManager.event;


import com.pack.Laetitia.service.serv.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    @Autowired
    private MailService emailService;

    @EventListener
    public void onUserEvent(UserEvent event) {
        switch(event.getType()){

            case REGISTRATION -> emailService.sendNewAccountEmail(
                    event.getUser().getFirstName(),
                    event.getUser().getEmail(),
                    (String)event.getData().get("key")
            );
            case RESETPASSWORD -> emailService.sendPasswordResetEmail(
                    event.getUser().getFirstName(),
                    event.getUser().getEmail(),
                    (String)event.getData().get("key")
            );
            default -> throw new IllegalArgumentException("Unknown user event type: " + event.getType());
        }
    }
}
