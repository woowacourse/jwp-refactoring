package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.OrderStatusValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusValidatorImpl implements OrderStatusValidator {

    private final OrderRepository orderRepository;

    public OrderStatusValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusNotCompletion(final List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                Arrays.asList(OrderStatus.MEAL, OrderStatus.COOKING));
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusNotCompletion(final Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(OrderStatus.MEAL, OrderStatus.COOKING));
    }
}
