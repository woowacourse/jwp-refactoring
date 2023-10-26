package kitchenpos.fixture;

import kitchenpos.domain.ordertable.OrderTable;

import java.util.List;

import static kitchenpos.fixture.FixtureUtil.listAllInDatabaseFrom;

@SuppressWarnings("NonAsciiCharacters")
public abstract class OrderTableFixture {

    @InDatabase
    public static OrderTable 테이블1() {
        return new OrderTable(1L);
    }

    @InDatabase
    public static OrderTable 테이블2() {
        return new OrderTable(2L);
    }

    @InDatabase
    public static OrderTable 테이블3() {
        return new OrderTable(3L);
    }

    @InDatabase
    public static OrderTable 테이블4() {
        return new OrderTable(4L);
    }

    @InDatabase
    public static OrderTable 테이블5() {
        return new OrderTable(5L);
    }

    @InDatabase
    public static OrderTable 테이블6() {
        return new OrderTable(6L);
    }

    @InDatabase
    public static OrderTable 테이블7() {
        return new OrderTable(7L);
    }

    @InDatabase
    public static OrderTable 테이블8() {
        return new OrderTable(8L);
    }

    public static OrderTable 테이블9() {
        return new OrderTable(9L);
    }


    public static List<OrderTable> listAllInDatabase() {
        return listAllInDatabaseFrom(OrderTableFixture.class, OrderTable.class);
    }
}
