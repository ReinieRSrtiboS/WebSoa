package websoa.ticket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import websoa.ticket.daos.Event;

import java.util.Optional;

@Slf4j
@Component
public class EventUpdater {
    private final RestTemplate rest = new RestTemplate();

    @Autowired
    private EventRegistry registry;

    @Scheduled(fixedRate = 30000)
    public void sync() {
        log.info("Updating event data");
        try {
            Event[] events = rest.getForEntity("http://event-service/events", Event[].class).getBody();
            if (events == null) return;

            log.info("Retrieved {} events", events.length);
            for (Event event : events) {
                Optional<Event> record = registry.find(event.id);
                if (record.isPresent()) {
                    log.info("Updated event {}", event.id);
                    record.get().tickets = event.tickets;
                } else {
                    log.info("Created event {}", event.id);
                    registry.insert(event);
                }
            }
        } catch (RestClientException exception) {
            log.warn("Failed to load event information: {}", exception.getMessage());
        }
    }
}
