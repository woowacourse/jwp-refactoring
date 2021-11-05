package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    private static final Long ID = null;
    private static final Long TABLE_GROUP_ID = null;
    private static final int NUMBER_OF_GUESTS = 1;
    private static final boolean EMPTY = false;

    public static OrderTable create() {
        return create(ID, TABLE_GROUP_ID, NUMBER_OF_GUESTS, EMPTY);
    }

    public static OrderTable create(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }
}
