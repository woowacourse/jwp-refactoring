package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable 테이블(boolean empty) {
        return new OrderTable(null, 0, empty);
    }

    public static OrderTable 테이블(boolean empty, int numberOfGuests) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public static OrderTable 테이블(boolean empty, int numberOfGuests, long tableGroupId) {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }
}
