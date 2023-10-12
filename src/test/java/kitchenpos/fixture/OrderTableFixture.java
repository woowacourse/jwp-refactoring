package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 주문테이블_1명() {
        final var orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(false);
        return orderTable;
    }

    public static OrderTable 빈테이블_1명() {
        final var orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable 주문테이블_5명() {
        final var orderTable = new OrderTable();
        orderTable.setNumberOfGuests(5);
        orderTable.setEmpty(false);
        return orderTable;
    }
}
