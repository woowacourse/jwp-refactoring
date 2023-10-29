package kitchenpos.domain;

import kitchenpos.domain.repository.OrderRepository;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusCheckListener {

    private final OrderRepository orderRepository;

    public OrderStatusCheckListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void checkOrderStatus(final OrderStatusCheckEvent event) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(event.getOrderTableId(),
            List.of(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("요리중이거나 식사중인 주문 테이블은 상태를 변경할 수 없습니다.");
        }
    }
}
