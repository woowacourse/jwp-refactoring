package kitchenpos.common.fixture;

import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 단체_지정_없는_주문_테이블() {
        return new OrderTable(null, 0, false);
    }

    public static OrderTable 단체_지정_없는_빈_주문_테이블() {
        return new OrderTable(null, 0, true);
    }

    public static OrderTable 빈_주문_테이블(Long tableGroupId) {
        return new OrderTable(tableGroupId, 0, true);
    }

    public static OrderTable 단체_지정_없는_빈_주문_테이블(int numberOfGuests) {
        return new OrderTable(null, numberOfGuests, true);
    }

    public static OrderTable 주문_테이블(Long tableGroupId) {
        return new OrderTable(tableGroupId, 0, false);
    }

    public static OrderTable 단체_지정_없는_주문_테이블(int numberOfGuests) {
        return new OrderTable(null, numberOfGuests, false);
    }

    public static OrderTable 주문_테이블(Long orderTableId, Long tableGroupId) {
        return new OrderTable(orderTableId, tableGroupId, 0, false);
    }
}
