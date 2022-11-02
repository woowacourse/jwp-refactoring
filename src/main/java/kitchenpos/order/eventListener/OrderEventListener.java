package kitchenpos.order.eventListener;

import java.util.Arrays;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.event.CheckTableCanChangeEvent;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;

@Component
public class OrderEventListener {

    private final OrderRepository orderRepository;

    public OrderEventListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void validateIsPossibleToChange(CheckTableCanChangeEvent event) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                event.getOrderTableIds(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리중이나 식사중인 주문이 존재합니다.");
        }
    }
}
