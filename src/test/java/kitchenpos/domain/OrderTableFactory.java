package kitchenpos.domain;

public final class OrderTableFactory {

    private OrderTableFactory() {
    }

    public static OrderTable createOrderTableOf(final int numberOfGuest, boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuest);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
