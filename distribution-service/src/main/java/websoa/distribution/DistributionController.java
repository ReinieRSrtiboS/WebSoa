package websoa.distribution;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import websoa.distribution.daos.Event;
import websoa.distribution.daos.Ticket;
import websoa.distribution.daos.User;

@Slf4j
@RestController
public class DistributionController {

    @Autowired
    private JavaMailSender mailer;

    @PostMapping("/send/{ticket_id}")
    public String send(@PathVariable String ticket_id) {
        RestTemplate template = new RestTemplate();

        Ticket ticket = template.getForEntity("http://ticket-service:8080/ticket/{ticket}", Ticket.class, ticket_id).getBody();
        User user = template.getForEntity("http://user-service:8080/user/{user}", User.class, ticket.user_id).getBody();
        Event event = template.getForEntity("http://event-service:8080/events/{event}", Event.class, ticket.event_id).getBody();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("event-system@websoa.ut");
        message.setTo(user.email);
        message.setSubject("Your ticket for " + event.name);
        message.setText(String.format("Hey %s,\nHere is your ticket for %s!\nExcept, not really, this is just a demo.\nGL;HF!", user.name, event.name));

        mailer.send(message);
        return "DONE!"; // TODO
    }
}
