package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 빈_신규_테이블() {
        return new OrderTable(1L, null, 0, true);
    }

    public static OrderTable 비지않은_신규_테이블() {
        return new OrderTable(2L, null, 0, false);
    }

    public static OrderTable 단일_조리_테이블() {
        return new OrderTable(1L, null, 2, false);
    }

    public static OrderTable 단일_식사_테이블() {
        return new OrderTable(2L, null, 3, false);
    }

    public static OrderTable 단일_계산완료_테이블() {
        return new OrderTable(3L, null, 4, false);
    }

    public static OrderTable 그룹핑된_조리_테이블() {
        return new OrderTable(1L, 1L, 2, false);
    }

    public static OrderTable 그룹핑된_식사_테이블() {
        return new OrderTable(2L, 2L, 3, false);
    }

    public static OrderTable 그룹핑된_계산완료_테이블() {
        return new OrderTable(3L, 3L, 4, false);
    }
}
