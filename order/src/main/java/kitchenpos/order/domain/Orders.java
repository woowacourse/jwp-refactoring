package kitchenpos.order.domain;

import java.util.List;

public class Orders {

    private List<Order> values;

    public Orders(List<Order> values) {
        this.values = values;
    }

    public void validateChangeEmpty() {
        if (isNotAllCompleted()) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isNotAllCompleted() {
        return values.stream().anyMatch(Order::isNotCompleted);
    }
}
