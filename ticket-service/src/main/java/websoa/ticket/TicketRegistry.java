package websoa.ticket;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.Ticket;
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

    public void activate(String ticket_id) { // Wordt nu niet lange termijn opgeslagen maar dat is TODO
        TicketInfo ticket = this.tickets.get(ticket_id);
        ticket.activated = true;
    }

    public HttpStatus new_event(String event_id, float price, int amount) {
        if (eventTickets.containsKey(event_id)) {
            return HttpStatus.CONFLICT;
        }
        eventTickets.putIfAbsent(event_id, new ArrayList<>(1));
        for (int i = tickets.size(); i < tickets.size() + amount; i++) {
            TicketInfo ticket = new TicketInfo(event_id, price, String.valueOf(i + 1));
            tickets.put(String.valueOf(i + 1), ticket);
            this.eventTickets.get(event_id).add(ticket);
        }
        return HttpStatus.OK;
    }

    public boolean reserve(String event_id, int amount) {
        List<TicketInfo> tickets = this.eventTickets.get(event_id);
        int reserved_no = 0;
        List<TicketInfo> reserve = new ArrayList<>();
        for (TicketInfo ticket : tickets) {
            if (!ticket.reserved) {
                ticket.reserved = true;
                reserve.add(ticket);
                reserved_no++;
                if (reserved_no == amount) {
                    return true;
                }
            }
        }
        for (TicketInfo ticket : reserve) {
            ticket.reserved = false;
        }
        return false;
    }
}
