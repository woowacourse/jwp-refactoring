package kitchenpos.domain.fixture;

import kitchenpos.domain.OrderTable;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTableFixture {

    public static OrderTable 주문_테이블_생성() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        return orderTable;
    }

}