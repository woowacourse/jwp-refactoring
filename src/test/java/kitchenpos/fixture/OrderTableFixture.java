package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableFixture {

    public static OrderTable 주문_테이블(final int guests, final boolean isEmpty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(guests);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }

    public static OrderTable 주문_테이블(final TableGroup tableGroup, final int guests, final boolean isEmpty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroup.getId());
        orderTable.setNumberOfGuests(guests);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }
}
