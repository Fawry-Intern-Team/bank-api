package com.example.bank_service.component;

import com.example.bank_service.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.OrderFailedEvent;
import org.example.events.PaymentCompletedEvent;
import org.example.events.StockReservedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockReservedListener {

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.STOCK_RESERVED_QUEUE)
    public void onStockReserved(StockReservedEvent event) {
        System.out.println("💳 Received StockReservedEvent for order: " + event.getOrderId());

        // Simulate payment logic
        boolean paymentSuccess = mockChargeCustomer(event.getOrderId());

        if (paymentSuccess) {
            PaymentCompletedEvent paymentEvent = new PaymentCompletedEvent(
                    event.getOrderId(),
                    UUID.randomUUID().toString()
            );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PAYMENT_COMPLETED_QUEUE,
                    paymentEvent
            );
        } else {
            OrderFailedEvent fail = new OrderFailedEvent(
                    event.getOrderId(),
                    "Payment failed"
            );

            rabbitTemplate.convertAndSend("order.failed","", fail);
        }
    }

    private boolean mockChargeCustomer(Long orderId) {
        // Simulate success/failure (e.g., 90% success)
        return Math.random() > 0.1;
    }

    @RabbitListener(queues = "bank.order.failed")
    public void onOrderFailed(OrderFailedEvent event) {
        log.info("❌ Order " + event.getOrderId() + " marked as FAILED. Reason: " + event.getReason());
    }
}
