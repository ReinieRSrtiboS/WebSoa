package websoa.validation;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import websoa.validation.daos.TicketInfo;

import java.util.Objects;

@Service
public class ValidationRegistry {

    public ResponseEntity<HttpStatus> validate(String ticket_id, String user_id) {
        RestTemplate rest = new RestTemplate();
        TicketInfo ticket = rest.getForObject("http://ticket-service/ticket/" + ticket_id, TicketInfo.class);
        if (!Objects.isNull(ticket) && ticket.user_id.equals(user_id) && ticket.paid && !ticket.activated) {
            return rest.exchange("http://ticket-service/activate/" + ticket_id, HttpMethod.PUT, new HttpEntity<>(HttpStatus.OK), HttpStatus.class);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
