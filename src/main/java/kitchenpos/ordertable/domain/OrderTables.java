package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.exception.TableGroupException;

import java.util.List;
import java.util.Objects;

public class OrderTables {

    private final static Integer MINIMUM_GROUP_SIZE = 2;

    private List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
    }

    public void group(Long tableGroupId) {
        for (OrderTable orderTable : orderTables) {
            orderTable.changeTableGroupId(tableGroupId);
        }
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (isNull(orderTables) || isLessThanMinimumGroupSize(orderTables)) {
            throw new TableGroupException("주문 테이블이 1개 이하라 테이블 그룹을 생성할 수 없습니다.");
        }
    }

    private boolean isNull(List<OrderTable> orderTables) {
        return Objects.isNull(orderTables);
    }

    private boolean isLessThanMinimumGroupSize(List<OrderTable> orderTables) {
        return orderTables.size() < MINIMUM_GROUP_SIZE;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
