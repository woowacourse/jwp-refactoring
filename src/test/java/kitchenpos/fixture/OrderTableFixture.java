package kitchenpos.fixture;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 빈_신규_테이블1() {
        return new OrderTable(1L, null, 0, true);
    }

    public static OrderTable 빈_신규_테이블2() {
        return new OrderTable(2L, null, 0, true);
    }

    public static OrderTable 그룹핑된_신규_테이블1() {
        TableGroup 테이블_그룹1 = TableGroupFixture.테이블_그룹1();
        return new OrderTable(1L, 테이블_그룹1.getId(), 0, false);
    }

    public static OrderTable 그룹핑된_신규_테이블2() {
        TableGroup 테이블_그룹1 = TableGroupFixture.테이블_그룹1();
        return new OrderTable(2L, 테이블_그룹1.getId(), 0, false);
    }

    public static OrderTable 비지않은_신규_테이블() {
        return new OrderTable(2L, null, 0, false);
    }

    public static OrderTable 단일_신규_테이블() {
        return new OrderTable(3L, null, 4, false);
    }

    public static OrderTable 단일_조리_테이블() {
        return new OrderTable(1L, null, 2, false);
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
}
