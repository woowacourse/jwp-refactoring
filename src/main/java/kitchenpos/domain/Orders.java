package kitchenpos.domain;

import java.util.List;
import org.springframework.data.relational.core.mapping.MappedCollection;

public class Orders {

    @MappedCollection(idColumn = "id")
    private List<Order> orders;

    public Orders(final List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public boolean hasNotCompletion() {
        return orders.stream()
            .anyMatch(order -> !order.isCompleted());
    }
}
