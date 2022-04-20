package websoa.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
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
        return this.render("login");
    }

    @GetMapping("/sign-up")
    public String sign_up() {
        return this.render("sign-up");
    }

    @GetMapping("/user/{id}")
    public User user(@PathVariable String id) {
        return this.registry.user(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @GetMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password) {
        Context context = new Context();

        Optional<User> user = this.registry.get_name(name);
        if (user.isPresent() && user.get().password.equals(password)) {
            RestTemplate rest = new RestTemplate();
            return rest.getForObject("http://events-service/" + user.get().id, String.class);
        } else {
            context.setVariable("tried", true);
            return this.render("login", context);
        }
    }

    @PostMapping("/create")
    public String create(@RequestParam String name, @RequestParam String password, @RequestParam String phone, @RequestParam String email) {
        Context context = new Context();

        if (this.registry.create(name, password, phone, email)) {
            context.setVariable("created", true);
            return this.render("login", context);
        } else {
            context.setVariable("tried", true);
            return this.render("sign-up", context);
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id) {
        Context context = new Context();
        context.setVariable("user", this.registry.user(id).get());
        return this.render("edit", context);
    }

    @PostMapping("/edited/{id}")
    public String update(@PathVariable String id, @RequestParam String name, @RequestParam String password, @RequestParam String phone, @RequestParam String email) {
        Context context = new Context();

        if (this.registry.update(id, name, password, phone, email)) {
            context.setVariable("user", this.registry.user(id).get());
            return this.render("testing", context);
        } else {
            context.setVariable("tried", true);
            context.setVariable("user", this.registry.user(id).get());
            return this.render("edit", context);
        }
    }

    private String render(String name) {
        return this.render(name, new Context());
    }

    private String render(String name, Context context) {
        StringWriter writer = new StringWriter();
        this.template.process(name, context, writer);
        return writer.toString();
    }
}
