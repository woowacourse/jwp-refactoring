package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTables;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusValidator implements OrderTableUpGroupValidator {

    private final OrderRepository orderRepository;

    public OrderStatusValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(final OrderTables orderTables) {
        final List<Long> orderTableIds = orderTables.getOrderTableIds();

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
