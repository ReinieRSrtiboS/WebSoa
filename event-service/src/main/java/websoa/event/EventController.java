package websoa.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import websoa.event.daos.Event;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import websoa.event.daos.TicketInfo;
import websoa.event.daos.User;

import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;

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

    @GetMapping("/{user_id}")
    public String showEvents(@PathVariable String user_id) {
        StringWriter writer = new StringWriter();
        Context context = new Context();


        RestTemplate rest = new RestTemplate();
        User user = rest.getForObject("http://user-service/user/" + user_id, User.class);

        context.setVariable("user", user);

        context.setVariable("events", this.registry.events());
        template.process("show-events", context, writer);
        return writer.toString();
    }

    @GetMapping("/order/{event_id}/{user_id}")
    public String order(@PathVariable String event_id, @PathVariable String user_id) {
        StringWriter writer = new StringWriter();
        Context context = new Context();

        RestTemplate rest = new RestTemplate();
//        TicketInfo[] result = rest.getForObject("http://ticket-service/tickets/" + event_id, TicketInfo[].class);
        User user = rest.getForObject("http://user-service/user/" + user_id, User.class);
        int amount = rest.getForObject("http://ticket-service/event/" + event_id + "/available", int.class);

//        context.setVariable("tickets", result.length);
        context.setVariable("user", user);
        context.setVariable("tickets", amount);
        context.setVariable("event", this.registry.event(event_id).get());
        template.process("buy-tickets", context, writer);
        return writer.toString();
    }

    @GetMapping("/buy/{event}/{user}")
    public String buy(@PathVariable String event, @PathVariable String user, @RequestParam int tickets) {

        RestTemplate rest = new RestTemplate();
        ResponseEntity<HttpStatus> answer = rest.exchange("http://ticket-service/reserve/" + event + "/" + tickets + "/" + user,
            HttpMethod.PUT, new HttpEntity<>(HttpStatus.OK), HttpStatus.class);

        if (answer.getStatusCode().is2xxSuccessful()) {
            String name = this.registry.event(event).get().name;
            return "You have successfully reserved " + tickets + " tickets for " + name + ". \r\n" +
                "After you have paid, the tickets will be sent to you per email";
        } else {
            return answer.getStatusCode().toString();
        }
    }

    @PostMapping("create/{name}/{price}/{amount}")
    public ResponseEntity<HttpStatus> create(@PathVariable String name, @PathVariable float price, @PathVariable int amount) {
        return registry.create(name, price, amount);
    }
}
