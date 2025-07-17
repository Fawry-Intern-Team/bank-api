package com.example.bank_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String COUPON_APPLIED_QUEUE = "coupon.applied.queue";
    public static final String PAYMENT_COMPLETED_QUEUE = "payment.completed.queue";
    public static final String ORDER_FAILED_QUEUE = "bank.order.failed";
    @Bean
    public FanoutExchange orderFailedExchange() {
        return new FanoutExchange("order.failed");
    }
    @Bean
    public Queue orderFailedQueue() {
        return new Queue(ORDER_FAILED_QUEUE);
    }

    @Bean
    public Binding exchangeBinding(FanoutExchange orderFailedExchange) {
        return BindingBuilder.bind(orderFailedQueue()).to(orderFailedExchange);
    }
    @Bean
    public Queue couponAppliedQueue() {
        return new Queue(COUPON_APPLIED_QUEUE);
    }

    @Bean
    public Queue paymentCompletedQueue() {
        return new Queue(PAYMENT_COMPLETED_QUEUE);
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
