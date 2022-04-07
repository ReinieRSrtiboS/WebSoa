package websoa.ticket.daos;

public class TicketInfo {
    public String id;
    public String event_id;
    public String user_id;
    public boolean reserved;
    public boolean bought;
    public boolean activated;
    public float price;

    public TicketInfo(String event_id, float price, String id) {
        this.event_id = event_id;
        this.price = price;
        this.id = id;
        this.reserved = false;
        this.bought = false;
        this.activated = false;
        this.user_id = "-1";
    }
}
