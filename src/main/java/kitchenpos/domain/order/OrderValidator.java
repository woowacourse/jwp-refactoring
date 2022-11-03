package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTables;

    public OrderValidator(final OrderTableRepository orderTables) {
        this.orderTables = orderTables;
    }

    public void validateOnCreate(final Order order) {
        validateOrderLineItemsNotEmpty(order);

        final var orderTable = orderTables.get(order.getOrderTableId());
        validateOrderTableNotEmpty(orderTable);
    }

    private void validateOrderLineItemsNotEmpty(final Order order) {
        if (order.getOrderLineItems().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
