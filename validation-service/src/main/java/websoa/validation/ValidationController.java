package websoa.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ValidationController {

    @Autowired
    private ValidationRegistry registry;

    @GetMapping("/validate/{ticket_id}/{user_id}")
    public boolean validate(@PathVariable String ticket_id, @PathVariable String user_id) {
        return registry.validate(ticket_id, user_id);
    }

}
