package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableFixture {

    public static OrderTable 주문_테이블(final Long id, final TableGroup tableGroup, final int numberOfGuests,
                                    final boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }
}
