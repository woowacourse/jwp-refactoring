package kitchenpos.support;

import kitchenpos.table.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable 비어있는_주문_테이블 = 주문_테이블_생성(null, 2, true);
    public static OrderTable 비어있지_않은_주문_테이블 = 주문_테이블_생성(null, 2, false);

    public static OrderTable 주문_테이블_생성(final Long tableGroupId, final Integer numberOfGuests, final Boolean isEmpty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }


}
