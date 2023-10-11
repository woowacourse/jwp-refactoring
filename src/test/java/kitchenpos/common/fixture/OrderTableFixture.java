package kitchenpos.common.fixture;

import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 주문_테이블(Long tableGroupId) {
        return new OrderTable(tableGroupId, 0, true);
    }

    public static OrderTable 주문_테이블(Long orderTableId, Long tableGroupId) {
        return new OrderTable(orderTableId, tableGroupId, 0, true);
    }
}
