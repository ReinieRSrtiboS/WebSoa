package websoa.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import websoa.payment.daos.PaymentRequest;
import websoa.payment.daos.PaymentResponse;

import java.io.StringWriter;
import java.net.URI;

@Slf4j
@RestController
public class PaymentController {
    @Autowired
    private TemplateEngine template;

    @Autowired
    private QueueReceiver queue;

    @GetMapping(value = "/")
    public String list() {
        StringWriter writer = new StringWriter();
        Context context = new Context();

        context.setVariable("requests", this.queue.pending());
        template.process("list", context, writer);
        return writer.toString();
    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<?> accept(@PathVariable String id) {
        PaymentRequest request = this.queue.pop(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        PaymentResponse response = PaymentResponse.fromRequest(request);
        response.success();
        queue.respond(response);
        log.info("Accepted payment request {}", request.id);
        return this.redirect("/payment/");
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<?> reject(@PathVariable String id) {
        PaymentRequest request = this.queue.pop(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        PaymentResponse response = PaymentResponse.fromRequest(request);
        response.failed();
        queue.respond(response);
        log.info("Rejected payment request {}", request.id);
        return this.redirect("/payment/");
    }

    private ResponseEntity<?> redirect(String uri) {
        return ResponseEntity
            .status(HttpStatus.FOUND)
            .location(URI.create(uri))
            .build();
    }
}
