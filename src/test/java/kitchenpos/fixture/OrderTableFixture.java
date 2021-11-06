package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableFixture {

    private static final Long ID = null;
    private static final TableGroup TABLE_GROUP = null;
    private static final int NUMBER_OF_GUESTS = 1;
    private static final boolean EMPTY = false;

    public static OrderTable create() {
        return create(ID, TABLE_GROUP, NUMBER_OF_GUESTS, EMPTY);
    }

    public static OrderTable create(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(id, tableGroup, numberOfGuests, empty);

        return orderTable;
    }
}
