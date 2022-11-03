package kitchenpos.domain.table;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    public void validateOnUngroup(final List<Order> orders) {
        final var allOrderCompleted = orders.stream()
                .allMatch(order -> order.getOrderStatus() == OrderStatus.COMPLETION);
        if (!allOrderCompleted) {
            throw new IllegalArgumentException();
        }
    }
}
