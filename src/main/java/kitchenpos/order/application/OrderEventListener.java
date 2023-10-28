package kitchenpos.order.application;

import java.util.Arrays;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dto.OrderStatusValidated;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final OrderRepository orderRepository;

    public OrderEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void handleOrderStatusEvent(final OrderStatusValidated orderStatusValidated) {
        final Long orderTableId = orderStatusValidated.getOrderTableId();
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문 테이블은 존재하면서 결제완료 상태가 아니어야 합니다.");
        }
    }
}
