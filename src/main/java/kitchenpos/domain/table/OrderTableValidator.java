package kitchenpos.domain.table;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderRepository orders;

    public OrderTableValidator(final OrderRepository orders) {
        this.orders = orders;
    }

    public void validateOnChangeEmpty(final OrderTable orderTable) {
        validateNotGrouped(orderTable);

        final var ordersInTable = orders.getByOrderTableId(orderTable.getId());
        validateAllOrderCompleted(ordersInTable);
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
