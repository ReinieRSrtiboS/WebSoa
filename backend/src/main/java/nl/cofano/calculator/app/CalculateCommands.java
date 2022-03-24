package nl.cofano.calculator.app;

import nl.cofano.calculator.integrations.task.CalculatorTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class CalculateCommands {

    @Autowired private JmsTemplate jmsTemplate;
    @Value("${queue.multiply}") private String multiplyQueue;

    @ShellMethod("Multiply two numbers")
    public String multiply(
      double a,
      double b
    ) {
        CalculatorTask task = new CalculatorTask();
        task.setNumber1(a);
        task.setNumber2(b);
        jmsTemplate.convertAndSend(multiplyQueue, task);

        return "Requested multiply";
    }
}
