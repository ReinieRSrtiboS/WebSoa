package websoa.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.StringWriter;

@RestController
public class ValidationController {

    @Autowired
    private TemplateEngine template;

    @Autowired
    private ValidationRegistry registry;

    @GetMapping("/validate")
    public String validate(@RequestParam String ticket_id, @RequestParam String user_id) {
        ResponseEntity<HttpStatus> answer = registry.validate(ticket_id, user_id);
        StringWriter writer = new StringWriter();
        Context context = new Context();
        if (answer.getStatusCode().is2xxSuccessful()) {
            context.setVariable("success", true);
        } else {
            context.setVariable("success", false);
        }
        template.process("validate", context, writer);
        return writer.toString();
    }

    @GetMapping("/validate_main")
    public String validate_main() {
        StringWriter writer = new StringWriter();
        Context context = new Context();

        template.process("validate_main", context, writer);
        return writer.toString();
    }

}
