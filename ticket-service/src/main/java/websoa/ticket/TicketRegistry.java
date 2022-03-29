package websoa.ticket;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
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

    private Map<String, TicketInfo> tickets;

    @PostConstruct
    public void postConstruct() throws Exception {
        Gson parser = new Gson();
        InputStreamReader isr = new InputStreamReader(dataFile.getInputStream());
        TicketInfo[] tickets = parser.fromJson(isr, TicketInfo[].class);

        this.tickets = new HashMap<>(tickets.length);
        for (TicketInfo ticket : tickets) {
            this.tickets.put(ticket.id, ticket);
        }
    }

    public Optional<Collection<TicketInfo>> event_tickets(String event_id) { // TODO kan vast in 1 regel, maar niet door mij
        Collection<TicketInfo> result = new ArrayList<>();
        for (TicketInfo ticket : tickets.values()) {
            if (event_id.equals(ticket.event_id)) {
                result.add(ticket);
            }
        }
        if (result.size() == 0) { // TODO dit is echt lelijke code, excuses
            return Optional.empty();
        }
        return Optional.of(result);
    }

    public Optional<TicketInfo> ticket(String id) {
        return Optional.ofNullable(tickets.get(id));
    }
}
