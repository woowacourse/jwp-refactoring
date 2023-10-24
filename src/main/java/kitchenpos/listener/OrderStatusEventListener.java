package kitchenpos.listener;

import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.dto.OrderStatusValidateByIdEvent;
import kitchenpos.table.dto.OrderStatusValidateByIdsEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusEventListener {

    private final OrderRepository orderRepository;

    public OrderStatusEventListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void validateOrderStatus(final OrderStatusValidateByIdEvent event) {
        orderRepository.findByOrderTableId(event.getOrderTableId())
                .ifPresent(order -> order.validateIsNotComplete());
    }

    @EventListener
    public void validateOrderStatusByOrderIds(final OrderStatusValidateByIdsEvent event) {
        orderRepository.findAllByOrderTableIdIn(event.getOrderTableIds()).stream()
                .forEach(order -> order.validateIsNotComplete());
    }
}


