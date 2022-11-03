package kitchenpos.domain.table;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    public void validateOnChangeEmpty(final OrderTable orderTable, final List<Order> orders) {
        validateNotGrouped(orderTable);
        validateAllOrderCompleted(orders);
    }

    private void validateNotGrouped(final OrderTable orderTable) {
        if (orderTable.getTableGroupId() != null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateAllOrderCompleted(final List<Order> orders) {
        final var allOrdersCompleted = orders.stream()
                .allMatch(order -> order.getOrderStatus() == OrderStatus.COMPLETION);
        if (!allOrdersCompleted) {
            throw new IllegalArgumentException();
        }
    }
}
