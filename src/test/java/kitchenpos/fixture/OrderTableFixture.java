package kitchenpos.fixture;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public OrderTable 주문_테이블_생성(Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public OrderTable 주문_테이블_생성(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public List<OrderTable> 주문_테이블_리스트_생성(OrderTable... orderTable) {
        return Arrays.asList(orderTable);
    }
}
