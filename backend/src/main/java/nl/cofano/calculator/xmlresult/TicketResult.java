package nl.cofano.calculator.xmlresult;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="ticketdata")
public class TicketResult {

    @XmlElement(name = "ticket")
    private List<TicketItem> results = new ArrayList<>();

    public List<TicketItem> getResults() {
        return results;
    }

    public static class TicketItem {
        @XmlElement
        private int id;

        @XmlElement
        private int eventid;

        @XmlElement
        private int userid;

        @XmlElement
        private boolean activated;

        @XmlElement
        private boolean bought;

        public int getId() {
            return id;
        }

        public int getEventid() {
            return eventid;
        }

        public int getUserid() {
            return userid;
        }

        public boolean isBought() {
            return bought;
        }

        public boolean isActivated() {
            return activated;
        }
    }
}


