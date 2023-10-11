package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    private OrderTableFixture() {
    }

    public static class ORDER_TABLE {
        public static OrderTable 주문_테이블_1() {
            final OrderTable orderTable = new OrderTable();
            orderTable.setId(1L);
            orderTable.setTableGroupId(1L);
            orderTable.setNumberOfGuests(3);
            orderTable.setEmpty(true);
            return orderTable;
        }

        public static OrderTable 주문_테이블_2() {
            final OrderTable orderTable = new OrderTable();
            orderTable.setId(2L);
            orderTable.setTableGroupId(1L);
            orderTable.setNumberOfGuests(3);
            orderTable.setEmpty(true);
            return orderTable;
        }
    }
}
