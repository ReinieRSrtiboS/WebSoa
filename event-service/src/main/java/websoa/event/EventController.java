package websoa.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import websoa.event.daos.Event;

import java.util.Collection;
import java.util.Optional;

@RestController
public class EventController {
    @Autowired
    private EventRegistry registry;

    @GetMapping("/events")
    public Collection<Event> events() {
        return this.registry.events();
    }

    @GetMapping("/events/{id}")
    public Event event(@PathVariable String id) {
        return this.registry.event(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }
}
