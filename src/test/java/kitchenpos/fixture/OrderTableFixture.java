package kitchenpos.fixture;

import kitchenpos.domain.order.OrderTable;

public class OrderTableFixture {

    public static OrderTable 주문테이블(Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }
}
