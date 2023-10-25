package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable 주문_테이블(final Long id, final Long tableGroupId, final int numberOfGuests,
                                    final boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }
}
