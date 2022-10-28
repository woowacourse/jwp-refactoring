package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable generateOrderTable(Long tableGroupId, int numberOfGuest, boolean isEmpty) {
        return new OrderTable(tableGroupId, numberOfGuest, isEmpty);
    }
}
