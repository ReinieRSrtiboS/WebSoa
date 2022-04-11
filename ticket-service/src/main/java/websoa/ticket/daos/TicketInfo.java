package websoa.ticket.daos;

public class TicketInfo {
    public String id;
    public String event_id;
    public String user_id;
    public boolean paid = false;
    public boolean activated = false;

    public TicketInfo(String event_id, String id) {
        this.event_id = event_id;
        this.id = id;
    }
}
