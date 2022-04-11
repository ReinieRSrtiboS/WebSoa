package websoa.validation;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ValidationRegistry {

    public ResponseEntity<HttpStatus> validate(String ticket_id, String user_id) {
        RestTemplate rest = new RestTemplate();
        return rest.exchange("http://ticket-service/activate/" + ticket_id + "/" + user_id,
            HttpMethod.PUT, new HttpEntity<>(HttpStatus.OK), HttpStatus.class);
    }
}
