package kitchenpos.fixture;

import kitchenpos.domain.table.OrderTable;

public class OrderTableFixtrue {
    public static OrderTable orderTable(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable orderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }

}
