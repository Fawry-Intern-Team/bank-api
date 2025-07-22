package com.example.bank_service.component;

import com.example.bank_service.config.RabbitMQConfig;
import com.example.bank_service.dto.TransactionRequestDTO;
import com.example.bank_service.dto.TransactionResponseDTO;
import com.example.bank_service.service.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.OrderCreatedEvent;
import org.example.events.OrderFailedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockReservedListener {

    private final RabbitTemplate rabbitTemplate;
    private final BankService bankService;

    @RabbitListener(queues = RabbitMQConfig.COUPON_APPLIED_QUEUE)
    public void onStockReserved(OrderCreatedEvent event) {
        try {
            TransactionRequestDTO requestDTO= TransactionRequestDTO.builder()
                    .cardNumber("123456789")
                    .amount(event.getTotalAmount())
                    .build();
            TransactionResponseDTO responseDTO = bankService.withdraw(requestDTO);
            event.setTransactionId(responseDTO.getTransactionId());
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PAYMENT_COMPLETED_QUEUE,
                    event
            );
        } catch (RuntimeException e) {
            OrderFailedEvent fail = new OrderFailedEvent(
                    event.getOrderId(),
                    "Payment failed",
                    event.getTransactionId()
            );
            rabbitTemplate.convertAndSend("order.failed", "", fail);
        }
    }


    @RabbitListener(queues = "bank.order.failed")
    public void onOrderFailed(OrderFailedEvent event) {
        log.info("❌ Order {} marked as FAILED. Reason: {}", event.getOrderId(), event.getReason());
    }
}
