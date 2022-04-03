package websoa.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import websoa.payment.daos.PaymentRequest;
import websoa.payment.daos.PaymentResponse;

@Slf4j
@Component
public class QueueReceiver {
    @Autowired
    private JmsTemplate template;

    @JmsListener(destination = "payment_requests")
    public void receive(PaymentRequest request) {
        log.info("Received payment request {}", request.id);

        // TODO make the result truly asynchronous, maybe even with user interaction (shell or web interface)
        PaymentResponse response = PaymentResponse.fromRequest(request);
        if (Math.random() > 0.5) response.success();
        else response.failed();

        template.convertAndSend("payment_results", response);
    }
}
