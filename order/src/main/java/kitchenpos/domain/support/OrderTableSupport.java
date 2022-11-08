package kitchenpos.domain.support;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.validator.OrderExistenceChecker;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableSupport implements OrderExistenceChecker {
    private final OrderRepository orderRepository;

    public OrderTableSupport(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean hasCookingOrMealOrderByOrderTableId(final Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, List.of(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}
