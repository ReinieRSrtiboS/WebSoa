package websoa.event.daos;

public class Event {
    public String id;
    public String name;
    public float price;
    public int tickets;

    public Event(String id, String name, float price, int tickets) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.tickets = tickets;
    }
}
