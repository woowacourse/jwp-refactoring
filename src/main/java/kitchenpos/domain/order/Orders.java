package kitchenpos.domain.order;

import java.util.List;

public class Orders {
    private List<Order> values;

    public Orders(List<Order> values) {
        this.values = values;
    }

    public void validateChangeEmpty() {
        if (isNotAllCompletion()) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isNotAllCompletion() {
        return values.stream()
                .anyMatch(order -> order.getOrderStatus().equals(OrderStatus.COOKING.name())
                        || order.getOrderStatus().equals(OrderStatus.MEAL.name()));
    }
}
