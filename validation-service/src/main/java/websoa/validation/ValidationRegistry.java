package websoa.validation;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import websoa.validation.TicketInfo.TicketInfo;

@Service
public class ValidationRegistry {

    public ResponseEntity<HttpStatus> validate(String ticket_id, String user_id) { // TODO make 1 call to ticket service?
        RestTemplate rest = new RestTemplate();
        TicketInfo result = rest.getForObject("http://ticket-service/ticket/" + ticket_id, TicketInfo.class);
        if (user_id.equals(result.user_id) && !result.activated) {
            return rest.exchange("http://ticket-service/activate/" + ticket_id, HttpMethod.PUT, new HttpEntity<>(result), HttpStatus.class);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
