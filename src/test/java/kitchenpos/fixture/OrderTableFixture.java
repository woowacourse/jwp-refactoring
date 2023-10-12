package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

import java.util.List;

import static kitchenpos.fixture.FixtureUtil.listAllInDatabaseFrom;
import static kitchenpos.fixture.FixtureUtil.pushing;

@SuppressWarnings("NonAsciiCharacters")
public abstract class OrderTableFixture {

    @InDatabase
    public static OrderTable 테이블1() {
        var orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
        return orderTable;
    }

    @InDatabase
    public static OrderTable 테이블2() {
        return pushing(new OrderTable(), 2L, null, 0, true);
    }

    @InDatabase
    public static OrderTable 테이블3() {
        return pushing(new OrderTable(), 3L, null, 0, true);
    }

    @InDatabase
    public static OrderTable 테이블4() {
        return pushing(new OrderTable(), 4L, null, 0, true);
    }

    @InDatabase
    public static OrderTable 테이블5() {
        return pushing(new OrderTable(), 5L, null, 0, true);
    }

    @InDatabase
    public static OrderTable 테이블6() {
        return pushing(new OrderTable(), 6L, null, 0, true);
    }

    @InDatabase
    public static OrderTable 테이블7() {
        return pushing(new OrderTable(), 7L, null, 0, true);
    }

    @InDatabase
    public static OrderTable 테이블8() {
        return pushing(new OrderTable(), 8L, null, 0, true);
    }

    public static OrderTable 테이블9() {
        return pushing(new OrderTable(), 9L, null, 0, true);
    }


    public static List<OrderTable> listAllInDatabase() {
        return listAllInDatabaseFrom(OrderTableFixture.class, OrderTable.class);
    }
}
