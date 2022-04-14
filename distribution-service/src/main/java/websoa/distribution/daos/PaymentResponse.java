package websoa.distribution.daos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"id", "success"})
@XmlRootElement(name = "PaymentResponse")
public class PaymentResponse {
    public String id;
    public boolean success;
}
