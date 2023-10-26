package kitchenpos.order.domain;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 단체_지정_없는_주문_테이블(boolean isEmpty) {
        return new OrderTable(null, 0, isEmpty);
    }

    public static OrderTable 단체_지정_없는_채워진_주문_테이블() {
        return new OrderTable(null, 0, false);
    }

    public static OrderTable 단체_지정_없는_빈_주문_테이블() {
        return new OrderTable(null, 0, true);
    }

    public static OrderTable 단체_지정_빈_주문_테이블(Long tableGroupId) {
        return new OrderTable(tableGroupId, 0, true);
    }

    public static OrderTable 단체_지정_주문_테이블(Long tableGroupId) {
        return new OrderTable(tableGroupId, 0, false);
    }

    public static OrderTable 단체_지정_주문_테이블(Long orderTableId, Long tableGroupId) {
        return new OrderTable(orderTableId, tableGroupId, 0, false);
    }

    public static OrderTable 단체_지정_없는_주문_테이블(Long orderTableId) {
        return new OrderTable(orderTableId, null, 0, false);
    }
}
