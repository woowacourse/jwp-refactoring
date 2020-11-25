package kitchenpos.application.verifier;

import static java.util.Arrays.*;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.domain.model.order.OrderRepository;
import kitchenpos.domain.model.order.OrderStatus;

@Component
public class UngroupTableVerifier {
    private final OrderRepository orderRepository;

    public UngroupTableVerifier(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void verify(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
