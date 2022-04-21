package websoa.distribution;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import websoa.distribution.daos.Event;
import websoa.distribution.daos.PaymentResponse;
import websoa.distribution.daos.Ticket;
import websoa.distribution.daos.User;

import java.util.Collections;

@Slf4j
@Component
@Scope("singleton")
public class QueueReceiver {
    @Autowired
    private JavaMailSender mailer;

    @Autowired
    private RestTemplate template;

    @JmsListener(destination = "payment_results")
    public void receive(PaymentResponse response) {
        log.info("Received payment result {}: {}", response.id, response.success ? "accepted" : "rejected");

        if (response.success) {
            Ticket ticket = template.patchForObject(
                "http://ticket-service/ticket/" + response.id,
                Collections.singletonMap("paid", true),
                Ticket.class
            );
            log.info("Updated ticket {}", ticket.id);

            User user = template.getForEntity("http://user-service/user/{user}", User.class, ticket.user_id).getBody();
            Event event = template.getForEntity("http://event-service/events/{event}", Event.class, ticket.event_id).getBody();
            log.info("Retrieved details for {}", ticket.id);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("event-system@websoa.ut");
            message.setTo(user.email);
            message.setSubject("Your ticket for " + event.name);
            message.setText(String.format("Hey %s,\nHere is your ticket for %s!\n>> %s <<\nGL;HF!", user.name, event.name, ticket.id));
            log.info("Composed email for {}", ticket.id);

            mailer.send(message);
            log.info("Sent email for {}", ticket.id);
        } else {
            template.delete("http://ticket-service/ticket/" + response.id);
        }
    }
}
