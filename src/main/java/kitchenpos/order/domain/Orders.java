package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Orders {
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ORDER_TABLE_ID")
    private final List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    public boolean isUngroupable() {
        return this.orders.stream()
                .allMatch(Order::isUngroupable);
    }

    public void add(Order order) {
        if (orders.contains(order)) {
            throw new IllegalArgumentException("이미 추가된 주문입니다.");
        }
        this.orders.add(order);
    }

    public boolean hasCookingOrMeal() {
        return this.orders.stream()
                .anyMatch(Order::isCookingOrMeal);
    }
}
