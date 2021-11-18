package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

@Embeddable
public class Orders {

    @OneToMany(mappedBy = "orderTable")
    private List<Order> elements;

    public Orders() {
        this(new ArrayList<>());
    }

    public Orders(final List<Order> elements) {
        this.elements = elements;
    }

    public boolean contains(final Order order) {
        return elements.contains(order);
    }

    public boolean containsOrderStatusIn(final List<OrderStatus> orderStatuses) {
        return elements.stream()
            .anyMatch(order -> order.isOrderStatusIn(orderStatuses));
    }

    public void add(final Order order) {
        elements.add(order);
    }

    public List<Order> getElements() {
        return elements;
    }
}
