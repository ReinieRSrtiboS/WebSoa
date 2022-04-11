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

    public ResponseEntity<HttpStatus> new_event(String event_id, float price, int amount) {
        //TODO this doesn't work with new logic anymore
        return null;
//        if (eventTickets.containsKey(event_id)) {
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
//        eventTickets.putIfAbsent(event_id, new ArrayList<>(1));
//        for (int i = tickets.size(); i < tickets.size() + amount; i++) {
//            TicketInfo ticket = new TicketInfo(event_id, price, String.valueOf(i + 1));
//            tickets.put(String.valueOf(i + 1), ticket);
//            this.eventTickets.get(event_id).add(ticket);
//        }
//        return new ResponseEntity<>(HttpStatus.OK);
    }

    public boolean reserve(String event_id, int amount) {
        Event event = eventRegistry.find(event_id).orElseThrow(() -> new RuntimeException("Could not find requested event"));
        int totalTickets = event.tickets;
        int usedTickets = eventTickets.get(event_id).size();
        int available = totalTickets - usedTickets;

        log.info("Attempting to reserve tickets for event {}, [T={}, U={}, A={}]", event_id, totalTickets, usedTickets, available);
        if (amount > available) return false;

        eventTickets.putIfAbsent(event_id, new ArrayList<>(amount));
        List<TicketInfo> indexEntry = eventTickets.get(event_id);

        for (int i = 0; i < amount; i++) {
            String id = String.format("E%s-T%d", event_id, indexEntry.size());
            TicketInfo ticket = new TicketInfo(event_id, id);
            paymentService.request(id, event.price);
            tickets.put(id, ticket);
            indexEntry.add(ticket);
        }

        return true;
    }
}
