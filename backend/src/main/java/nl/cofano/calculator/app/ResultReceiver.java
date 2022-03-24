package nl.cofano.calculator.app;

import nl.cofano.calculator.integrations.response.CalculatorResponse;
import nl.cofano.calculator.integrations.task.CalculatorTask;
import org.apache.activemq.command.ActiveMQMessage;
import org.jline.terminal.Terminal;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
public class ResultReceiver {

    @Autowired
    private Terminal terminal;

    @JmsListener(destination = "${queue.results}")
    public void receiveMessage(CalculatorResponse calculatorResponse) {
        terminal.writer().println();
        terminal.writer().write(calculatorResponse.getCalculationString());
        terminal.writer().println();
        terminal.writer().flush();
    }
}
