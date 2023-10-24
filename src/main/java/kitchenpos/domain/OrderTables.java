package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public class OrderTables {

    private List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        this.orderTables = orderTables;
    }

    public static OrderTables from(List<OrderTable> orderTables, int idSize){
        validateIdSize(orderTables, idSize);
        validateHasTableGroupOrEmpty(orderTables);
        return new OrderTables(orderTables);
    }

    private static void validateHasTableGroupOrEmpty(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }
    }

    private static void validateIdSize(List<OrderTable> orderTables, int idSize) {
        if (orderTables.size() != idSize) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
