package websoa.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import websoa.payment.daos.PaymentRequest;
import websoa.payment.daos.PaymentResponse;

import java.util.*;

@Slf4j
@Component
@Scope("singleton")
public class QueueReceiver {
    private Map<String, PaymentRequest> requests = new HashMap<>();

    @Autowired
    private JmsTemplate template;

    @JmsListener(destination = "payment_requests")
    public void receive(PaymentRequest request) {
        log.info("Received payment request {}", request.id);
        requests.put(request.id, request);
    }

    public void respond(PaymentResponse response) {
        template.convertAndSend("payment_results", response);
    }

    public Collection<PaymentRequest> pending() {
        return this.requests.values();
    }

    public Optional<PaymentRequest> pop(String id) {
        PaymentRequest request = this.requests.get(id);
        if (request == null) return Optional.empty();
        this.requests.remove(id);
        return Optional.of(request);
    }
}
