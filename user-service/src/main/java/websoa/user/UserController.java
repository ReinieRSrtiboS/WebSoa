package websoa.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public String main(Model model) {
        StringWriter writer = new StringWriter();
        Context context = new Context();

        template.process("login", context, writer);
        return writer.toString();
    }

    @GetMapping("/user/{id}")
    public User user(@PathVariable String id) {
        return this.registry.user(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @GetMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password) {
        return name;
    }

    // TODO Update
    // TODO Create
}
