package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable 주문테이블(Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable 주문테이블_비움상태_변경(OrderTable orderTable, boolean empty) {
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable 주문테이블_손님수_변경(OrderTable orderTable, int numberOfGuests) {
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }
}
