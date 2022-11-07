package kitchenpos.domain;

import java.util.List;

public class OrderTables {

    private List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public boolean hasOrderTableWhichStatusIsCookingOrMeal() {
        return orderTables.stream()
                .anyMatch(orderTable -> orderTable.isCookingOrMeal());
    }

    public void unregisterTableGroup() {
        orderTables.forEach(OrderTable::unregisterTableGroup);
    }
}
