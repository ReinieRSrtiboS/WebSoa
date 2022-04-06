package websoa.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import websoa.event.daos.Event;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import websoa.event.daos.TicketInfo;

import java.io.StringWriter;
import java.util.Collection;

@RestController
public class EventController {

    @Autowired
    private TemplateEngine template;

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

//    @GetMapping("/")
//    public String showEvents(Model model) {
//        model.addAttribute("events", registry.events());
//        return "show-events";
//    }
    // Methode hierboven werkte niet dus heb het zo maar gedaan
    @GetMapping("/")
    public String showEvents(Model model) { // TODO get available tickets instead of all of bedenk een loop erom heen
    StringWriter writer = new StringWriter();
    Context context = new Context();

    context.setVariable("events", this.registry.events());
    template.process("show-events", context, writer);
    return writer.toString();
    }

    @GetMapping("/order/{event_id}")
    public String order(@PathVariable String event_id) {
        StringWriter writer = new StringWriter();
        Context context = new Context();

        RestTemplate rest = new RestTemplate();
        TicketInfo[] result = rest.getForObject("http://ticket-service/tickets/" + event_id, TicketInfo[].class);
//        int[] result = {1, 2, 3};

        context.setVariable("tickets", result.length);
        template.process("buy-tickets", context, writer);
        return writer.toString();
    }
}
