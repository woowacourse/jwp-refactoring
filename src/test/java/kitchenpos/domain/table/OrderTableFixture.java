package kitchenpos.domain.table;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 단체_지정_없는_주문_테이블() {
        return new OrderTable(null, 0, false);
    }

    public static OrderTable 단체_지정_없는_빈_주문_테이블() {
        return new OrderTable(null, 0, true);
    }

    public static OrderTable 빈_주문_테이블(TableGroup tableGroup) {
        return new OrderTable(tableGroup, 0, true);
    }

    public static OrderTable 주문_테이블(TableGroup tableGroup) {
        return new OrderTable(tableGroup, 0, false);
    }

    public static OrderTable 주문_테이블(Long orderTableId, TableGroup tableGroup) {
        return new OrderTable(orderTableId, tableGroup, 0, false);
    }
}
