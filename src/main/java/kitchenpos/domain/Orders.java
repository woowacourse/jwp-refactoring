package kitchenpos.domain;

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
        this.orders = orders;
    }

    public boolean hasProceedingOrder() {
        return orders.stream()
            .anyMatch(order -> !order.isCompleted());
    }

    public List<Order> getOrders() {
        return orders;
    }
}
