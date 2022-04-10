package websoa.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import websoa.ticket.daos.TicketInfo;

import java.util.Collection;
import java.util.Optional;

@RestController
public class TicketController {

    @Autowired
    private TicketRegistry registry;

    @GetMapping("/tickets/{event_id}")
    public Collection<TicketInfo> event_tickets(@PathVariable String event_id) {
        return this.registry.event_tickets(event_id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

    }

    @GetMapping("/ticket/{id}")
    public TicketInfo ticket(@PathVariable String id) {
        return this.registry.ticket(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
    }

    @PutMapping("/activate/{ticket_id}")
    public ResponseEntity<HttpStatus> validate(@PathVariable String ticket_id) { // TODO give error status if goes wrong
        this.registry.activate(ticket_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/reserve/{event_id}/{amount}")
    public ResponseEntity<HttpStatus> reserve(@PathVariable String event_id, @PathVariable int amount) {
        if (this.registry.reserve(event_id, amount)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/create/{event_id}/{price}/{amount}")
    public ResponseEntity<HttpStatus> create(@PathVariable String event_id, @PathVariable float price, @PathVariable int amount) {
        return this.registry.new_event(event_id, price, amount);
    }

}
