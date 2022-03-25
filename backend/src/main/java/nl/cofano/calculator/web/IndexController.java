package nl.cofano.calculator.web;

import nl.cofano.calculator.domain.Ticket;
import nl.cofano.calculator.services.PaymentService;
import nl.cofano.calculator.services.TicketService;
import nl.cofano.calculator.xmlresult.TicketResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/")
    public String showMain(Model model){
        return "main";
    }

    @GetMapping("/tickets")
    public ResponseEntity<TicketResult> getTickets(){
        TicketResult result = ticketService.getTickets();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<Ticket> getTickets(@PathVariable("id") int id){
        Ticket ticket = ticketService.getTicket(id);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }
}
