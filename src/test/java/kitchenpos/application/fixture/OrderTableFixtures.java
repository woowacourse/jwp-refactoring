package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixtures {

    public static final OrderTable generateOrderTable(final int numberOfGuests, final boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static final OrderTable 테이블_1번() {
        return generateOrderTable(0, true);
    }

    public static final OrderTable 테이블_2번() {
        return generateOrderTable(0, true);
    }

    public static final OrderTable 테이블_3번() {
        return generateOrderTable( 0, true);
    }

    public static final OrderTable 테이블_4번() {
        return generateOrderTable(0, true);
    }

    public static final OrderTable 테이블_5번() {
        return generateOrderTable(0, true);
    }

    public static final OrderTable 테이블_6번() {
        return generateOrderTable(0, true);
    }

    public static final OrderTable 테이블_7번() {
        return generateOrderTable(0, true);
    }

    public static final OrderTable 테이블_8번() {
        return generateOrderTable(0, true);
    }
}
