package websoa.ticket;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import websoa.ticket.daos.Event;
import websoa.ticket.daos.TicketInfo;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@Scope("singleton")
public class TicketRegistry {
    @Value("classpath:tickets.json")
    private Resource dataFile;

    @Autowired
    private EventRegistry eventRegistry;
    @Autowired
    private PaymentService paymentService;

    private Map<String, List<TicketInfo>> eventTickets = new HashMap<>();
    private Map<String, AtomicInteger> counters = new HashMap<>();
    private Map<String, TicketInfo> tickets;

    @PostConstruct
    public void postConstruct() throws Exception {
        Gson parser = new Gson();
        InputStreamReader isr = new InputStreamReader(dataFile.getInputStream());
        TicketInfo[] tickets = parser.fromJson(isr, TicketInfo[].class);

        this.tickets = new HashMap<>(tickets.length);
        for (TicketInfo ticket : tickets) {
            this.tickets.put(ticket.id, ticket);
            this.eventTickets.putIfAbsent(ticket.event_id, new ArrayList<>(1));
            this.eventTickets.get(ticket.event_id).add(ticket);
        }
    }

    public Optional<Collection<TicketInfo>> event_tickets(String event_id) {
        return Optional.ofNullable(this.eventTickets.get(event_id));
    }

    public Optional<TicketInfo> ticket(String id) {
        return Optional.ofNullable(tickets.get(id));
    }

    public int get_available(String event_id) {
        Event event = eventRegistry.find(event_id).orElseThrow(() -> new RuntimeException("Could not find requested event"));
        int totalTickets = event.tickets;
        if (eventTickets.containsKey(event_id)) {
            int usedTickets = eventTickets.get(event_id).size();
            return totalTickets - usedTickets;
        } else {
            return totalTickets;
        }
    }

    public boolean reserve(String event_id, int amount, String user_id) {
        Event event = eventRegistry.find(event_id).orElseThrow(() -> new RuntimeException("Could not find requested event"));

        if (amount > this.get_available(event_id)) return false;

        eventTickets.putIfAbsent(event_id, new ArrayList<>(amount));
        counters.putIfAbsent(event_id, new AtomicInteger(1));
        List<TicketInfo> indexEntry = eventTickets.get(event_id);
        AtomicInteger counter = counters.get(event_id);

        for (int i = 0; i < amount; i++) {
            String id = String.format("E%s-T%d", event_id, counter.getAndIncrement());
            TicketInfo ticket = new TicketInfo(event_id, id, user_id);
            paymentService.request(id, event.price);
            tickets.put(id, ticket);
            indexEntry.add(ticket);
        }

        return true;
    }

    public void remove(String id) {
        TicketInfo ticket = this.tickets.get(id);
        this.eventTickets.get(ticket.event_id).remove(ticket);
        this.tickets.remove(ticket.id);
    }

    public void insertEvent(Event event) {
        this.eventTickets.putIfAbsent(event.id, new ArrayList<>(0));
    }
}
