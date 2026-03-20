package messiasproject.microservicespecialist.order.config.bean.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${exchange.recive.order.name}")
    private String reciveOrderExchange;

    @Value("${queues.recive.order.name}")
    private String reciveOrderQueue;

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(reciveOrderExchange, true, false);
    }

    @Bean
    public Queue queueReciveOrder(){
        return new Queue(reciveOrderQueue, true);
    }

    @Bean
    public Binding bindReciveOrderQueue(FanoutExchange exchange, Queue reciveOrderQueue) {
        return BindingBuilder.bind(reciveOrderQueue).to(exchange);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
