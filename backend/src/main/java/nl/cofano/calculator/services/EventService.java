package nl.cofano.calculator.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class EventService {

    public ResponseEntity<Boolean> newEvent(int amount, int id) {
        URI uri = null;
        try {
            uri = new URI("http://localhost:8080/tickets/" + amount + "/" + id);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // TODO execute URL
        return null;
    }
}
