package domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {

    @OneToMany(mappedBy = "orderTable")
    private final List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    public Orders(final List<Order> orders) {
        this.orders.addAll(new ArrayList<>(orders));
    }

    public static Orders EMPTY_ORDERS() {
        return new Orders();
    }

    public void add(final Order order) {
        this.orders.add(order);
    }

    public boolean hasStatusOf(final OrderStatus status) {
        return orders.stream().anyMatch(order -> order.isStatusOf(status));
    }

    public boolean isAllStatusOf(final OrderStatus status) {
        return orders.stream().allMatch(order -> order.isStatusOf(status));
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }

    public List<Order> getOrders() {
        return orders;
    }
}
