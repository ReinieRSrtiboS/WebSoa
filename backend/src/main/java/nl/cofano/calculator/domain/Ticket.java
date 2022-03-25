package nl.cofano.calculator.domain;

public class Ticket {

    private int id;
    private int event_id;
    private int user_id;
    private boolean bought;
    private boolean activated;

    public Ticket(int id, int event_id) {
        this.id = id;
        this.event_id = event_id;
        bought = false;
        activated = false;
    }

    public Ticket(int id, int event_id, int user_id) {
        this.id = id;
        this.event_id = event_id;
        this.user_id = user_id;
        bought = true;
        activated = false;
    }

    public int getId(){
        return id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public boolean isActivated() {
        return activated;
    }

    public boolean isBought() {
        return bought;
    }
}
