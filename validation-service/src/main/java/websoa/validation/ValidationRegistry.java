package websoa.validation;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import websoa.validation.TicketInfo.TicketInfo;

@Service
public class ValidationRegistry {

    public boolean validate(String ticket_id, String user_id) {
        RestTemplate rest = new RestTemplate();
        TicketInfo result = rest.getForObject("http://ticket-service/ticket/" + ticket_id, TicketInfo.class);
        if (user_id.equals(result.user_id) && !result.activated) {
            // TODO set ticket already seen
            return true;
        } else {
            return false;
        }
    }
}
