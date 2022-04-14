package websoa.user.daos;

public class User {
    public String id;
    public String name;
    public String password;
    public String email;
    public String phone;

    public User(String id, String name, String password, String email, String phone) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }
}
