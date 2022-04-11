package websoa.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import websoa.ticket.daos.PaymentRequest;

@Service
public class PaymentService {
    @Autowired
    private JmsTemplate template;

    public void request(String id, float amount) {
        PaymentRequest request = new PaymentRequest(id, amount);
        template.convertAndSend("payment_requests", request);
    }
}
