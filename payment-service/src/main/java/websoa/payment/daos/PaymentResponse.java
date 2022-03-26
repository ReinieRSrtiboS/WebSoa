package websoa.payment.daos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"id", "success"})
@XmlRootElement(name = "PaymentResponse")
public class PaymentResponse {
    public static PaymentResponse fromRequest(PaymentRequest request) {
        PaymentResponse response = new PaymentResponse();
        response.id = request.id;
        return response;
    }

    public String id;
    public boolean success;

    public void success() {
        this.success = true;
    }

    public void failed() {
        this.success = false;
    }
}
