package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable create() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);

        return orderTable;
    }

}
