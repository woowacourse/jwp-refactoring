package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static final OrderTable ORDER_TABLE_FIXTURE_1 = new OrderTable();
    public static final OrderTable ORDER_TABLE_FIXTURE_2 = new OrderTable();
    public static final OrderTable ORDER_TABLE_FIXTURE_3 = new OrderTable();
    public static final OrderTable ORDER_TABLE_FIXTURE_4 = new OrderTable();
    public static final OrderTable ORDER_TABLE_FIXTURE_5 = new OrderTable();
    public static final OrderTable ORDER_TABLE_FIXTURE_6 = new OrderTable();

    static {
        ORDER_TABLE_FIXTURE_1.setNumberOfGuests(4);
        ORDER_TABLE_FIXTURE_1.setEmpty(true);
        ORDER_TABLE_FIXTURE_2.setNumberOfGuests(5);
        ORDER_TABLE_FIXTURE_2.setEmpty(true);
        ORDER_TABLE_FIXTURE_3.setNumberOfGuests(6);
        ORDER_TABLE_FIXTURE_3.setEmpty(true);
        ORDER_TABLE_FIXTURE_4.setNumberOfGuests(7);
        ORDER_TABLE_FIXTURE_4.setEmpty(true);
        ORDER_TABLE_FIXTURE_5.setNumberOfGuests(8);
        ORDER_TABLE_FIXTURE_5.setEmpty(true);
        ORDER_TABLE_FIXTURE_6.setNumberOfGuests(9);
        ORDER_TABLE_FIXTURE_6.setEmpty(true);
    }
}
