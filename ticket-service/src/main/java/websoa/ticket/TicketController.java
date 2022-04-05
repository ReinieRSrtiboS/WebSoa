package websoa.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/activate/{ticket_id}") // Toen ik hier PutMapping van maakte kreeg ik een error dus snap het niet meer.
    public HttpStatus validate(@PathVariable String ticket_id) {
        this.registry.activate(ticket_id);
        return HttpStatus.OK;
    }
}
