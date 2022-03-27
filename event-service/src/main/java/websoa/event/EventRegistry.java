package websoa.event;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import websoa.event.daos.Event;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Scope("singleton")
public class EventRegistry {
    @Value("classpath:events.json")
    private Resource dataFile;

    private Map<String, Event> events;

    @PostConstruct
    public void postConstruct() throws Exception {
        Gson parser = new Gson();
        InputStreamReader isr = new InputStreamReader(dataFile.getInputStream());
        Event[] events = parser.fromJson(isr, Event[].class);

        this.events = new HashMap<>(events.length);
        for (Event event : events) {
            this.events.put(event.id, event);
        }
    }

    public Collection<Event> events() {
        return events.values();
    }

    public Optional<Event> event(String id) {
        return Optional.ofNullable(events.get(id));
    }
}
