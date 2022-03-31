package websoa.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import websoa.user.daos.User;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRegistry registry;

    @GetMapping("/user/{id}")
    public User user(@PathVariable String id) {
        Optional<User> user = this.registry.user(id);
        if (user.isPresent()) return user.get();
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
}
