package kitchenpos.domain.order;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.table.OrderStatusChecker;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusCheckerImpl implements OrderStatusChecker {

    private final OrderRepository orderRepository;

    public OrderStatusCheckerImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatusChangeable(final List<Long> orderIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderIds,
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
