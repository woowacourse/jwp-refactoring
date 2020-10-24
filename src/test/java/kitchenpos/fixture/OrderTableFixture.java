package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static final OrderTable ORDER_TABLE_FIXTURE_1 = new OrderTable();
    public static final OrderTable ORDER_TABLE_FIXTURE_2 = new OrderTable();
    public static final OrderTable ORDER_TABLE_FIXTURE_3 = new OrderTable();
    public static final OrderTable ORDER_TABLE_FIXTURE_4 = new OrderTable();

    static {
//        ORDER_TABLE_FIXTURE_1.setTableGroupId(1L);
        ORDER_TABLE_FIXTURE_1.setNumberOfGuests(4);
//        ORDER_TABLE_FIXTURE_2.setTableGroupId(2L);
        ORDER_TABLE_FIXTURE_2.setNumberOfGuests(5);
//        ORDER_TABLE_FIXTURE_3.setTableGroupId(3L);
        ORDER_TABLE_FIXTURE_3.setNumberOfGuests(6);
//        ORDER_TABLE_FIXTURE_4.setTableGroupId(1L);
        ORDER_TABLE_FIXTURE_4.setNumberOfGuests(7);
    }
}
