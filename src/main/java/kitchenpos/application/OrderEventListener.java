package kitchenpos.application;

import java.util.Arrays;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableEmptyChangeEvent;
import kitchenpos.domain.repository.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final OrderRepository orderRepository;

    public OrderEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    void validatePossibleToChangeOrderTableEmpty(final OrderTableEmptyChangeEvent event) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                event.getOrderTableId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리중 or 식사중인 주문이 포함되어 있습니다.");
        }
    }

}
