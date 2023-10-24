package kitchenpos.ordertable.domain.vo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.order.domain.Order;
import org.hibernate.annotations.BatchSize;

@Embeddable
public class Orders {
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "orderTable", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public boolean hasCookingOrMealOrders() {
        return orders.stream()
                .anyMatch(Order::isStatusCookingOrMeal);
    }

    public void add(final Order order) {
        this.orders.add(order);
    }

    public List<Order> orders() {
        return orders;
    }
}
