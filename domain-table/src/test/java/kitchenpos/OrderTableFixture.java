package kitchenpos;

import kitchenpos.domain.OrderTable;

import static kitchenpos.TableGroupFixture.GROUP1;

public class OrderTableFixture {
    public static OrderTable 단일_손님2_테이블 = new OrderTable(1L, null, 2, false);
    public static OrderTable 그룹1_손님2_테이블 = new OrderTable(2L, GROUP1, 2, false);
    public static OrderTable 단일_손님0_테이블1 = new OrderTable(3L, null, 0, true);
    public static OrderTable 단일_손님0_테이블2 = new OrderTable(4L, null, 0, true);
    public static OrderTable 그룹_손님0_테이블 = new OrderTable(5L, GROUP1, 0, true);
}
