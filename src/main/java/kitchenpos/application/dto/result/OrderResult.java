package kitchenpos.application.dto.result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;

public class OrderResult {

    private final Long id;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<MenuInOrderResult> menuResults;

    public OrderResult(
            final Long id,
            final String orderStatus,
            final LocalDateTime orderedTime,
            final List<MenuInOrderResult> menuResults
    ) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.menuResults = menuResults;
    }

    public static OrderResult from(final Order order) {
        return new OrderResult(
                order.getId(),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                order.getOrderLineItems().stream()
                        .map(MenuInOrderResult::from)
                        .collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<MenuInOrderResult> getMenuResults() {
        return menuResults;
    }
}
