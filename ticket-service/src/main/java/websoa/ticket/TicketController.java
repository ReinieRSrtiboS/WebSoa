package websoa.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import websoa.ticket.daos.TicketInfo;

import java.util.Collection;
import java.util.Optional;

@RestController
public class TicketController {

    @Autowired
    private TicketRegistry registry;

    @GetMapping("/tickets/{event_id}") // TODO misschien andere naam, dit kan colliden met specifieke ticket opvragen
    public Collection<TicketInfo> event_tickets(@PathVariable String event_id) {
        Optional<Collection<TicketInfo>> tickets = this.registry.event_tickets(event_id);
        if (tickets.isPresent()) return tickets.get();
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");

    }

    @GetMapping("/ticket/{id}")
    public TicketInfo ticket(@PathVariable String id) {
        Optional<TicketInfo> ticket = this.registry.ticket(id);
        if (ticket.isPresent()) return ticket.get();
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
    }
}
