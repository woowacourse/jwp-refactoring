package kitchenpos.fixture;

import kitchenpos.order_table.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable 주문_테이블_생성() {
        OrderTable orderTable = new OrderTable();
        orderTable.updateEmpty(true);
        return orderTable;
    }

}