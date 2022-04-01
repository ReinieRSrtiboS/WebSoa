package websoa.validation;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import websoa.validation.TicketInfo.TicketInfo;

@Service
public class ValidationRegistry {

    public boolean validate(String ticket_id, String user_id) {
        RestTemplate rest = new RestTemplate();
        TicketInfo result = rest.getForObject("http://localhost/ticket/" + ticket_id, TicketInfo.class); // TODO service id ofzo ipv localhost
        if (user_id.equals(result.user_id)) {
            // TODO set ticket already seen
            return true;
        } else {
            return false;
        }
    }
}
