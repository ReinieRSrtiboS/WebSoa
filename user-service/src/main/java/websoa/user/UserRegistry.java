package websoa.user;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import websoa.user.daos.User;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class UserRegistry {
    @Value("classpath:users.json")
    private Resource dataFile;

    private Map<String, User> users;

    @PostConstruct
    public void postConstruct() throws Exception {
        Gson parser = new Gson();
        InputStreamReader isr = new InputStreamReader(dataFile.getInputStream());
        User[] users = parser.fromJson(isr, User[].class);

        this.users = new HashMap<>(users.length);
        for (User user : users) {
            this.users.put(user.id, user);
        }
    }

    public Optional<User> user(String id) {
        return Optional.ofNullable(users.get(id));
    }
}
