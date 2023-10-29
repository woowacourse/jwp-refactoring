package kitchenpos.order.listener;

import java.util.Arrays;
import kitchenpos.common.OrderStatus;
import kitchenpos.common.event.OrderCheckEvent;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CheckStatusByTableListener {

    private final OrderRepository orderRepository;

    public CheckStatusByTableListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void existsByOrderTableIdAndOrderStatus(final OrderCheckEvent event) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                event.getOrderTableId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문이 아직 완료상태가 아닙니다.");
        }
    }
}
