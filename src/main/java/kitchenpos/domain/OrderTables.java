package kitchenpos.domain;

import java.util.List;
import kitchenpos.exception.InvalidOrderTablesException;
import kitchenpos.exception.OrderTableNotEmptyException;

public class OrderTables {

    private static final int MIN_COUNT = 2;

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        validateCount(this.orderTables);
    }

    private void validateCount(List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_COUNT) {
            throw new InvalidOrderTablesException(String.format("OrderTable는 최소 %s개 이상이어야 합니다.", MIN_COUNT));
        }
    }

    public void validateEmpty() {
        for (OrderTable orderTable : orderTables) {
            validateEmpty(orderTable);
        }
    }

    private void validateEmpty(OrderTable orderTable) {
        if (orderTable.isNotEmpty() || orderTable.isGrouped()) {
            throw new OrderTableNotEmptyException(String.format("%s OrderTable이 비어있지 않습니다.", orderTable.getId()));
        }
    }

    public void groupBy(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.groupBy(tableGroup);
        }
    }

    public List<OrderTable> toList() {
        return orderTables;
    }
}
