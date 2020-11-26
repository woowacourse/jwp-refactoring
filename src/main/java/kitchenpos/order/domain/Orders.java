package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderTables;

import java.util.List;

public class Orders {
    private final List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public void ungroup(OrderTables orderTables) {
        if (isUngroupable()) {
            orderTables.ungroup();
            return;
        }
        throw new IllegalArgumentException("단체 지정된 주문 테이블의 주문 상태가 조리 또는 식사인 경우 단체 지정을 해지할 수 없습니다.");
    }

    private boolean isUngroupable() {
        return this.orders.stream()
                .allMatch(Order::isUngroupable);
    }
}
