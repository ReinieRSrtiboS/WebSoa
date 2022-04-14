package websoa.distribution;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import websoa.distribution.daos.PaymentResponse;

@Slf4j
@Component
@Scope("singleton")
public class QueueReceiver {
    @JmsListener(destination = "payment_results")
    public void receive(PaymentResponse response) {
        log.info("Received payment result {}: {}", response.id, response.success ? "accepted" : "rejected");
        //TODO
    }
}
