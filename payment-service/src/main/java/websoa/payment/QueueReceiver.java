package websoa.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import websoa.payment.daos.PaymentRequest;
import websoa.payment.daos.PaymentResponse;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        request.age = LocalDateTime.now();
        requests.put(request.id, request);
    }

    public void respond(PaymentResponse response) {
        template.convertAndSend("payment_results", response);
    }

    @Scheduled(fixedRate = 300L)
    public void autoRespond() {
        Iterator<PaymentRequest> iterator = requests.values().iterator();
        LocalDateTime cutoff = LocalDateTime.now().minus(15, ChronoUnit.MINUTES);

        while (iterator.hasNext()) {
            if (iterator.next().age.isBefore(cutoff)) {
                iterator.remove();
            }
        }
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

    @PreDestroy
    public void onShutDown() {
        log.info("Resubmitting {} requests", requests.size());
        for (PaymentRequest request : requests.values()) {
            log.info("Resubmitting request {}", request.id);
            template.convertAndSend("payment_requests", request);
        }
    }
}
