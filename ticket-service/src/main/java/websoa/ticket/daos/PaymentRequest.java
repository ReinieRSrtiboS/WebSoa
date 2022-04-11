package websoa.ticket.daos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"id", "amount"})
@XmlRootElement(name = "PaymentRequest")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    public String id;
    public double amount;
}
