package kitchenpos.table.domain;

import kitchenpos.tablegroup.domain.TableGroup;

public class OrderTableFixture {

    public static OrderTable 주문_테이블(final Long id, final TableGroup tableGroup, final int numberOfGuests,
                                    final boolean empty) {
        if (tableGroup == null) {
            return new OrderTable(id, null, numberOfGuests, empty);
        }
        return new OrderTable(id, tableGroup.getId(), numberOfGuests, empty);
    }
}
