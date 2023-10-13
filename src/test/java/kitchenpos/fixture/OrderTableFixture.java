package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public final class OrderTableFixture {

    public static OrderTable 주문_테이블_생성() {
        final OrderTable orderTable = new OrderTable();

        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(0);

        return orderTable;
    }

    public static OrderTable 주문_테이블_생성(final boolean empty) {
        final OrderTable orderTable = new OrderTable();

        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(0);

        return orderTable;
    }

    public static OrderTable 주문_테이블_생성(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = new OrderTable();

        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }

    private OrderTableFixture() {
    }
}
