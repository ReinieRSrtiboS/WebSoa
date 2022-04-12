package websoa.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.StringWriter;

@RestController
public class AdminController {

    @Autowired
    private TemplateEngine template;

    @GetMapping("/")
    public String main(Model model) {
        StringWriter writer = new StringWriter();
        Context context = new Context();

        template.process("main", context, writer);
        return writer.toString();
    }

    @GetMapping("/new_event")
    public String new_event() {
        StringWriter writer = new StringWriter();
        Context context = new Context();

        template.process("new_event", context, writer);
        return writer.toString();
    }

    @PostMapping("/create")
    public String create(@RequestParam String name, @RequestParam int price, @RequestParam int amount) {

        RestTemplate rest = new RestTemplate();
        ResponseEntity<HttpStatus> answer = rest.exchange("http://event-service/create/" + name + "/" + price + "/" + amount,
            HttpMethod.POST, new HttpEntity<>(HttpStatus.OK), HttpStatus.class);

        StringWriter writer = new StringWriter();
        Context context = new Context();

        if (answer.getStatusCode().is2xxSuccessful()) {
            template.process("created", context, writer);
        } else {
            template.process("failed", context, writer);
        }
        return writer.toString();
    }

    @GetMapping("/validate_main")
    public String validate() {
        RestTemplate rest = new RestTemplate();
        return rest.getForObject("http://validation-service/validate_main", String.class);
    }
}
