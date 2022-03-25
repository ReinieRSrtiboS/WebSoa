package nl.cofano.calculator.services;

import nl.cofano.calculator.domain.Ticket;
import nl.cofano.calculator.xmlresult.TicketResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

@Service
public class ValidationService {

    public boolean validate(int user_id, int ticket_id) {
        URI uri = null;
        try {
            uri = new URI("http://localhost:8080/tickets/" + ticket_id);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        RestTemplate rest = new RestTemplate();
        Ticket ticket = rest.getForObject(uri, Ticket.class);

        return ticket.getUser_id() == user_id;
    }
}
