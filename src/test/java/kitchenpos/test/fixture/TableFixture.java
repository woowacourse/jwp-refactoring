package kitchenpos.test.fixture;

import kitchenpos.domain.OrderTable;

public class TableFixture {

    public static OrderTable 테이블(Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
