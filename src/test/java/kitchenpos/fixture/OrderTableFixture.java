package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableCreateRequest;

public class OrderTableFixture {

    public static OrderTableCreateRequest generateOrderTableCreateRequest(int numberOfGuest, boolean isEmpty) {
        return new OrderTableCreateRequest(numberOfGuest, isEmpty);
    }

    public static OrderTable generateOrderTable(Long tableGroupId, int numberOfGuest, boolean isEmpty) {
        return new OrderTable(tableGroupId, numberOfGuest, isEmpty);
    }
}
