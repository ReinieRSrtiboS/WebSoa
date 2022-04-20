package websoa.ticket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import websoa.ticket.daos.TicketInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class TicketController {

    @Autowired
    private TicketRegistry registry;

    @GetMapping("/tickets/{event_id}")
    public Collection<TicketInfo> event_tickets(@PathVariable String event_id) {
        return this.registry.event_tickets(event_id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

    }

    @GetMapping("/event/{event_id}/available")
    public int available_event_tickets(@PathVariable String event_id) {
        return this.registry.get_available(event_id);
    }

    @GetMapping("/ticket/{id}")
    public TicketInfo ticket(@PathVariable String id) {
        return this.registry.ticket(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
    }

    @PatchMapping("/ticket/{id}")
    public TicketInfo ticket(@PathVariable String id, @RequestBody Map<String, Object> request) {
        TicketInfo ticket = this.registry.ticket(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
        if (request.containsKey("paid"))
            ticket.paid = (boolean) request.get("paid");
        return ticket;
    }

    @DeleteMapping("/ticket/{id}")
    public void deleteTicket(@PathVariable String id) {
        log.info("Removing ticket {}", id);
        this.registry.remove(id);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Ticket removed");
    }

    @PutMapping("/activate/{ticket_id}")
    public ResponseEntity<HttpStatus> validate(@PathVariable String ticket_id) {
        TicketInfo ticket = this.registry.ticket(ticket_id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
        ticket.activated = true;
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/reserve/{event_id}/{amount}/{user_id}")
    public ResponseEntity<HttpStatus> reserve(@PathVariable String event_id, @PathVariable int amount, @PathVariable String user_id) {
        log.info("Reserving {} tickets for {}", amount, event_id);
        if (this.registry.reserve(event_id, amount, user_id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

}
