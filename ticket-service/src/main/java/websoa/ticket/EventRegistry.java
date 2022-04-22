package websoa.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import websoa.ticket.daos.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Scope("singleton")
public class EventRegistry {

    @Autowired
    private TicketRegistry ticketRegistry;
    private final Map<String, Event> events = new HashMap<>();

    public Optional<Event> find(String id) {
        return Optional.ofNullable(events.get(id));
    }

    public void insert(Event event) {
        events.put(event.id, event);
        ticketRegistry.insertEvent(event);
    }
}
