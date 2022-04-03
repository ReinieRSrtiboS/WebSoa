package websoa.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import websoa.payment.daos.PaymentRequest;
import websoa.payment.daos.PaymentResponse;

import java.util.stream.Collectors;

@RestController
public class PaymentController {
    @Autowired
    private QueueReceiver queue;

    @PostMapping("/accept/{id}")
    public String accept(@PathVariable String id) {
        PaymentRequest request = this.queue.pop(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        PaymentResponse response = PaymentResponse.fromRequest(request);
        response.success();
        queue.respond(response);
        return "Accepted";
    }

    @PostMapping("/reject/{id}")
    public String reject(@PathVariable String id) {
        PaymentRequest request = this.queue.pop(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        PaymentResponse response = PaymentResponse.fromRequest(request);
        response.failed();
        queue.respond(response);
        return "Rejected";
    }
}
