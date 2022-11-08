package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class Validator implements kitchenpos.table.domain.Validator {

    private final OrderRepository orderRepository;

    public Validator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(final OrderTable orderTable) {
        final List<Order> orders = getOrders(orderTable);
        if (orders.isEmpty()) {
            return;
        }
        final boolean notCompletedOrder = orders.stream()
                .anyMatch(it -> !it.isComplete());
        if (notCompletedOrder) {
            throw new IllegalArgumentException();
        }
    }

    private List<Order> getOrders(final OrderTable orderTable) {
        return orderRepository.findAllByOrderTableId(orderTable.getId());
    }
}
