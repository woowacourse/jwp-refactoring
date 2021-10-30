package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

import static kitchenpos.fixture.TableGroupFixture.GROUP1;


public class OrderTableFixture {
    public static OrderTable 단일_손님2_테이블 = new OrderTable(1L, null, 2, false);
    public static OrderTable 그룹1_손님4_테이블 = new OrderTable(2L, GROUP1, 4, false);
    public static OrderTable 그룹1_손님2_테이블 = new OrderTable(3L, GROUP1, 2, false);
    public static OrderTable 단일_손님0_테이블 = new OrderTable(4L, null, 0, true);
}
