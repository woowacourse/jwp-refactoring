package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFactory {

    public static OrderTable createEmptyTable() {
        return new OrderTable(0, true);
    }

    public static OrderTable createOrderTable(int numberOfGuest, boolean empty) {
        return new OrderTable(numberOfGuest, empty);
    }

    public static OrderTable createChangeOrderTableRequest(boolean empty) {
        return new OrderTable(0, empty);
    }

    public static OrderTable createChangeOrderTableRequest(int numberOfGuest) {
        return new OrderTable(numberOfGuest, false);
    }
}
