package websoa.payment.daos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.time.LocalDateTime;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"id", "amount"})
@XmlRootElement(name = "PaymentRequest")
public class PaymentRequest {
    public String id;
    public double amount;
    public transient LocalDateTime age;
}
