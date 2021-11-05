package kitchenpos.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class Orders {

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders;

    public Orders() {
    }

    public void checkAllOrderCompleted() {
        for (Order order : orders) {
            checkOrderIsCompleted(order);
        }
    }

    private void checkOrderIsCompleted(Order order) {
        if (order.isCompleted()) {
            return;
        }
        throw new IllegalArgumentException();
    }
}
