package websoa.payment;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.util.Collections;

@EnableJms
@Configuration
public class QueueConfiguration {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(QueueConfiguration.class);

    @Value("${activemq.broker.url}")
    private String url;

    private final ResourceLoader resourceLoader;

    @Autowired
    public QueueConfiguration(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(url);
        log.info("Connecting to ActiveMQ host: {}", url);
        return activeMQConnectionFactory;
    }

    @Bean
    public MessageConverter jaxbMarshaller() {
        // New XML Marshaller
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan("websoa.payment.daos");
        jaxb2Marshaller.setSchema(resourceLoader.getResource("classpath:payment-service.xsd"));
        jaxb2Marshaller.setMarshallerProperties(Collections.singletonMap("jaxb.formatted.output", true));
        try {
            jaxb2Marshaller.afterPropertiesSet();
        } catch (Exception e) {
            log.error("Could not update marshaller properties", e);
        }

        // Configure it in the JMS messageconverter
        MarshallingMessageConverter converter = new MarshallingMessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setMarshaller(jaxb2Marshaller);
        converter.setUnmarshaller(jaxb2Marshaller);
        return converter;
    }


    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMQConnectionFactory());
        factory.setMessageConverter(jaxbMarshaller());
        factory.setConcurrency("3-10"); // limit concurrent listener
        factory.setErrorHandler((e) -> log.error("An error occurred while processing the message queue message", e));

        return factory;
    }
}
