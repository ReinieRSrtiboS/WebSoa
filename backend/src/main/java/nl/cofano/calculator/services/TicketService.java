package nl.cofano.calculator.services;

import nl.cofano.calculator.domain.Ticket;
import nl.cofano.calculator.xmlresult.TicketResult;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    public TicketResult getTickets() {
        return null;
    }

    public Ticket getTicket(int id) {
        return null;
    }

    public ResponseEntity<Boolean> newTickets(int amount, int eventId) {
        return null;
    }
}