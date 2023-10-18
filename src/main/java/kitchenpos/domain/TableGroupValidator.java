package kitchenpos.domain;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;

import java.util.List;
import java.util.Objects;

public abstract class TableGroupValidator {

    private TableGroupValidator() {
    }

    public static void validateGroupOrderTables(List<OrderTable> orderTables, List<Long> orderTableIds) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블을 지정했습니다. 단체 지정 할 수 없습니다.");
        }
        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException("비어있지 않은 테이블이거나 이미 단체지정된 테이블입니다. 단체 지정할 수 없습니다.");
            }
        }
    }

    public static void validateUngroupTableOrders(List<Order> orders) {
        for (Order order : orders) {
            if (order.isStatus(OrderStatus.COOKING) || order.isStatus(OrderStatus.MEAL)) {
                throw new IllegalArgumentException("테이블의 주문이 이미 조리 혹은 식사 중입니다. 단체 지정을 삭제할 수 없습니다.");
            }
        }
    }
}
