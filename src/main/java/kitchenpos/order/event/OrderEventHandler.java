package kitchenpos.order.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;

@Component
public class OrderEventHandler {

    private static final List<String> ORDER_STATUS_FOR_CANT_UNGROUP = new ArrayList<String>() {{
        add(OrderStatus.COOKING.name());
        add(OrderStatus.MEAL.name());
    }};

    private final OrderRepository orderRepository;

    public OrderEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    private void verifiedAbleToUngroup(VerifiedAbleToUngroupEvent event) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            event.getOrderTableIds(),
            ORDER_STATUS_FOR_CANT_UNGROUP)
        ) {
            throw new IllegalArgumentException("주문이 시작되어 그룹을 해제할 수 없습니다.");
        }
    }

}
