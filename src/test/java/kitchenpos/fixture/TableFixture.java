package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class TableFixture {

    public final static OrderTable 빈_테이블_1번 = new OrderTable(null, null, 1, true);
    public final static OrderTable 빈_테이블_2번 = new OrderTable(null, null, 2, true);
    public final static OrderTable 사용중인_테이블_1번 = new OrderTable(null, null, 3, false);
}
