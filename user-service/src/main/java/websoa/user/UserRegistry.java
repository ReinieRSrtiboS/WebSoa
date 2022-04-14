package websoa.user;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import websoa.user.daos.User;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class UserRegistry {
    @Value("classpath:users.json")
    private Resource dataFile;

    private Map<String, User> users;
    private Map<String, User> usernames;

    @PostConstruct
    public void postConstruct() throws Exception {
        Gson parser = new Gson();
        InputStreamReader isr = new InputStreamReader(dataFile.getInputStream());
        User[] users = parser.fromJson(isr, User[].class);

        this.users = new HashMap<>(users.length);
        this.usernames = new HashMap<>(users.length);
        for (User user : users) {
            this.users.put(user.id, user);
            this.usernames.put(user.name, user);
        }
    }

    public Optional<User> user(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public boolean password(String name, String password) {
        Optional<User> user = Optional.ofNullable(this.usernames.get(name));
        return user.map(value -> value.password.equals(password)).orElse(false);
    }

    public Optional<User> get_name(String name) {
        return Optional.ofNullable(this.usernames.get(name));
    }

    public boolean create(String name, String password, String phone, String email) {
        if (this.usernames.containsKey(name)) {
            return false;
        } else {
            User user = new User(String.valueOf(this.users.size() + 1), name, password, email, phone);
            this.users.put(user.id, user);
            this.usernames.put(name, user);
            return true;
        }
    }

    public boolean update(String id, String name, String  password, String phone, String email) {
        User user = this.users.get(id);
        if (this.usernames.containsKey(name) && !user.name.equals(name)) {
            return false;
        } else {
            user.name = name;
            user.phone = phone;
            user.email = email;
            if (!password.equals("")) {
                user.password = password;
            }
        }
        return true;
    }
}
