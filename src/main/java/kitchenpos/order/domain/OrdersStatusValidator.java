package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.OrderTablesValidator;
import org.springframework.stereotype.Component;

@Component
public class OrdersStatusValidator implements OrderTablesValidator {

    private final OrderRepository orderRepository;

    public OrdersStatusValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(final OrderTables orderTables) {
        for (final OrderTable orderTable : orderTables.getOrderTables()) {
            validateUngroupAvailable(orderTable.getId());
        }
    }

    public void validateUngroupAvailable(final Long orderTableId) {
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        if (!orders.stream().allMatch(Order::isCompleted)) {
            throw new IllegalArgumentException("Cannot ungroup non-completed table.");
        }
    }
}
