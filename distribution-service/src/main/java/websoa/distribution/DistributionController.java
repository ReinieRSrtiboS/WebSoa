package websoa.distribution;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DistributionController {

    @Autowired
    private JavaMailSender mailer;

    @PostMapping("/send/{ticket_id}")
    public String send(@PathVariable String ticket_id) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("event-system@websoa.ut");
        message.setTo("user-id-here@websoa.ut"); // TODO
        message.setSubject("Event Tickets"); // TODO
        message.setText("TICKETS FOR " + ticket_id); // TODO

        mailer.send(message);
        return "DONE!"; // TODO
    }
}
