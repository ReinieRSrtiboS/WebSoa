package websoa.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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

        template.process("admin", context, writer);
        return writer.toString();
    }

    @GetMapping("/new_event")
    public String new_event() {
        StringWriter writer = new StringWriter();
        Context context = new Context();

        template.process("new_event", context, writer);
        return writer.toString();
    }
}
