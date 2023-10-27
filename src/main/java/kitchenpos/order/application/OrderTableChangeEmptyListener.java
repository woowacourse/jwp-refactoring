package kitchenpos.order.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTableChangeEmptyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OrderTableChangeEmptyListener {

    private final OrderRepository orderRepository;

    public OrderTableChangeEmptyListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void validateOrderStatus(OrderTableChangeEmptyEvent orderTableChangeEmptyEvent) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableChangeEmptyEvent.getOrderTableId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리 중이거나 식사 중인 주문의 상태를 변경할 수 없습니다.");
        }
    }
}
