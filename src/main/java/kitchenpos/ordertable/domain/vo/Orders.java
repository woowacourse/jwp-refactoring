package kitchenpos.ordertable.domain.vo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.vo.OrderStatus;
import org.hibernate.annotations.BatchSize;

@Embeddable
public class Orders {
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "orderTable", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public boolean hasCookingOrMealOrders() {
        return orders.stream()
                .map(Order::orderStatus)
                .anyMatch(orderStatus -> orderStatus == OrderStatus.COOKING || orderStatus == OrderStatus.MEAL);
    }

    public List<Order> orders() {
        return orders;
    }
}
