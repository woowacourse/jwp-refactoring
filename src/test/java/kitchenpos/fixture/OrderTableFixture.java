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

    public static OrderTable 주문테이블_2명_id_1() {
        final var orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        return orderTable;
    }

    public static OrderTable 빈테이블_1명_단체지정() {
        final var orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(true);
        orderTable.setTableGroupId(1L);
        return orderTable;
    }
}
