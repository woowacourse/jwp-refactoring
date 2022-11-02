package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.order.repository.OrderRepository;

@Component
public class OrderTableValidator {
    private static final List<String> ORDER_STATUS_FOR_CANT_CHANGE_EMPTY = new ArrayList<String>() {{
        add(OrderStatus.COOKING.name());
        add(OrderStatus.MEAL.name());
    }};

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, ORDER_STATUS_FOR_CANT_CHANGE_EMPTY)) {
            throw new IllegalArgumentException("변경할 수 있는 상태가 아닙니다.");
        }
    }

}
