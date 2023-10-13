package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixtures {

    public static OrderTable createEmptyTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        return orderTable;
    }

}
