package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixtures {

    public static final OrderTable generateOrderTable(final int numberOfGuests, final boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static final OrderTable generateOrderTable(final Long tableGroupId,
                                                      final int numberOfGuests,
                                                      final boolean empty) {
        return generateOrderTable(null, tableGroupId, numberOfGuests, empty);
    }

    public static final OrderTable generateOrderTable(final Long id, final OrderTable orderTable) {
        return generateOrderTable(
                id,
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public static final OrderTable generateOrderTable(final Long id,
                                                      final Long tableGroupId,
                                                      final int numberOfGuests,
                                                      final boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
