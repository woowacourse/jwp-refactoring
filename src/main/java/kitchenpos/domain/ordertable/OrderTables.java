package kitchenpos.domain.ordertable;

import java.util.List;
import java.util.Objects;

public class OrderTables {

    private List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        validateOrderTableSize(orderTables);
        validateOrderTableIsNotEmptyOfAlreadyContainedOtherTableGroup(orderTables);
        this.orderTables = orderTables;
    }

    public void validateOrderTableSize(final List<OrderTable> orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹할 주문 테이블은 2개 이상이어야 합니다.");
        }
    }

    public void validateOrderTableIsNotEmptyOfAlreadyContainedOtherTableGroup(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException("테이블이 비어있지 않거나 이미 다른 그룹에 포함된 주문 테이블은 새로운 테이블 그룹에 속할 수 없습니다.");
            }
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
