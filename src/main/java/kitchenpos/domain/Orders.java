package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {

    @OneToMany(mappedBy = "orderTable", cascade = CascadeType.ALL)
    private List<Order> orders;

    protected Orders() {
    }

    public Orders(final List<Order> orders) {
        this.orders = new ArrayList<>(orders);
    }

    public void addOrder(final Order order, final OrderTable orderTable) {
        orders.add(order);
        order.joinOrderTable(orderTable);
    }

    public boolean hasProceedingOrder() {
        return orders.stream()
            .anyMatch(order -> !order.isCompleted());
    }

    public List<Order> getOrders() {
        return orders;
    }
}
