package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable 주문_테이블_생성(final Long tableGroupId,
                                       final int numberOfGuests,
                                       final boolean isEmpty) {
        return new OrderTable(tableGroupId, numberOfGuests, isEmpty);
    }
}
