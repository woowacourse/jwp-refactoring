package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import lombok.Getter;

@Getter
public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables ofNotGroupedOrderTables(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
        return new OrderTables(orderTables);
    }

    public void group(Long tableGroupId) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(tableGroupId);
            orderTable.setEmpty(false);
        }
    }

    public List<OrderTable> getValue() {
        return orderTables;
    }
}
