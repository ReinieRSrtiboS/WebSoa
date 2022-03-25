package websoa.payment;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import websoa.payment.daos.PaymentRequest;
import websoa.payment.daos.PaymentResponse;

@Component
public class QueueReceiver {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(QueueReceiver.class);

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
