package websoa.distribution;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class MailConfig {
    @Bean
    public JavaMailSender getSender() {
        JavaMailSenderImpl sender=new JavaMailSenderImpl();
        sender.setHost("mailhog");
        sender.setPort(1025);

        return sender;
    }
}
