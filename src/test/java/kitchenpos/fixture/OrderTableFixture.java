package kitchenpos.fixture;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 단체_지정이_있는_주문_테이블_생성(
            TableGroup tableGroup,
            int numberOfGuests,
            boolean empty
    ) {
        OrderTable orderTable = OrderTable.of(
                numberOfGuests,
                empty
        );

        orderTable.registerTableGroup(tableGroup.getId());
        return orderTable;
    }

    public static OrderTable 단체_지정이_없는_주문_테이블_생성(
            int numberOfGuests,
            boolean empty
    ) {
        return OrderTable.of(numberOfGuests, empty);
    }

}
