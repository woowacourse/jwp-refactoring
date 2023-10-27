package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public Orders(final List<Order> orders, final OrderTable orderTable) {
        validateOrderTable(orders);
        this.orders = new ArrayList<>(orders);
        this.orders.forEach(order -> order.registerOrderTable(orderTable.getId()));
    }

    private void validateOrderTable(final List<Order> orders) {
        final boolean hasNullOrderTable = orders.stream()
            .map(Order::getOrderTableId)
            .anyMatch(Objects::nonNull);

        if (hasNullOrderTable) {
            throw new IllegalArgumentException("이미 주문 테이블에 속한 주문은 추가할 수 없습니다.");
        }
    }

    public boolean hasProceedingOrder() {
        return orders.stream()
            .anyMatch(order -> !order.isCompleted());
    }

    public List<Order> getOrders() {
        return orders;
    }
}
