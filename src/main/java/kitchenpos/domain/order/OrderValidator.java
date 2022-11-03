package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    public void validateOnCreate(final Order order, final OrderTable orderTable) {
        validateOrderLineItemsNotEmpty(order);
        validateOrderTableNotEmpty(orderTable);
    }

    private void validateOrderTableNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItemsNotEmpty(final Order order) {
        if (order.getOrderLineItems().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
