package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.data.relational.core.mapping.MappedCollection;

public class Orders {

    @MappedCollection(idColumn = "ORDER_TABLE_ID", keyColumn = "ORDER_TABLE_KEY")
    private final List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public static Orders createEmptyOrders() {
        return new Orders(new ArrayList<>());
    }

    public boolean checkIncompleteOrders() {
        return orders.stream().anyMatch(Predicate.not(Order::isComplete));
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public void add(Order order) {
        orders.add(order);
    }

    public Order getNewOrder() {
        int newOrderIndex = orders.size() - 1;

        return orders.get(newOrderIndex);
    }
}
