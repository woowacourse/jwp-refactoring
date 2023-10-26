package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_table_id")
    private List<Order> orders;

    protected Orders() {
    }

    public Orders(final List<Order> orders) {
        this.orders = new ArrayList<>(orders);
    }

    public void addOrder(final Order order) {
        orders.add(order);
    }

    public boolean hasProceedingOrder() {
        return orders.stream()
            .anyMatch(order -> !order.isCompleted());
    }

    public List<Order> getOrders() {
        return orders;
    }
}
