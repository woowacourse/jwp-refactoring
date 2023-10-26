package kitchenpos.order.domain;

import java.util.List;
import java.util.Objects;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableValidator;
import org.springframework.stereotype.Component;

@Component
public class OrdersEmptyValidator implements OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrdersEmptyValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(final OrderTable orderTable) {
        validateGroupedTable(orderTable);
        validateOrdersStatus(orderTable);
    }

    private void validateGroupedTable(final OrderTable orderTable) {
        if (Objects.isNull(orderTable.getTableGroup())) {
            return;
        }
        throw new IllegalArgumentException("Cannot change empty status of table in group");
    }

    private void validateOrdersStatus(final OrderTable orderTable) {
        final Long orderTableId = orderTable.getId();
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        if (orders.stream().allMatch(Order::isCompleted)) {
            return;
        }
        throw new IllegalArgumentException("Cannot change empty status of table with order status not completion");
    }
}
