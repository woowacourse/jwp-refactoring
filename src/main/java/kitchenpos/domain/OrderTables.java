package kitchenpos.domain;

import java.util.List;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
    }

    public boolean matchSize(final int value) {
        return orderTables.size() == value;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("단체 테이블은 2 테이블 이상부터 가능합니다.");
        }
        orderTables.forEach(OrderTable::validateOrderTable);
    }
}
