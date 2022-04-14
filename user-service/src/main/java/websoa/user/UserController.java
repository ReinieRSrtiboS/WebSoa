package websoa.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import websoa.user.daos.User;

import java.io.StringWriter;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRegistry registry;

    @Autowired
    private TemplateEngine template;

    @GetMapping("/")
    public String main() {
        StringWriter writer = new StringWriter();
        Context context = new Context();

        template.process("login", context, writer);
        return writer.toString();
    }

    @GetMapping("/sign-up")
    public String sign_up() {
        StringWriter writer = new StringWriter();
        Context context = new Context();

        template.process("sign-up", context, writer);
        return writer.toString();
    }

    @GetMapping("/user/{id}")
    public User user(@PathVariable String id) {
        return this.registry.user(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @GetMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password) {
        StringWriter writer = new StringWriter();
        Context context = new Context();

        if (this.registry.password(name, password)) {
            return "success";
        } else {
            context.setVariable("tried", true);
            template.process("login", context, writer);
        }
        return writer.toString();
    }

    @PostMapping("/create")
    public String create(@RequestParam String name, @RequestParam String password, @RequestParam String phone, @RequestParam String email) {
        StringWriter writer = new StringWriter();
        Context context = new Context();

        if (this.registry.create(name, password, phone, email)) {
            template.process("login", context, writer);
            context.setVariable("created", true);
        } else {
            context.setVariable("tried", true);
            template.process("sign-up", context, writer);
        }
        return writer.toString();
    }

    // TODO Update
}
