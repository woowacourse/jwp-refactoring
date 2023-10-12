package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    public static TableGroup 테이블_그룹_엔티티_A_주문_테이블_0개 = createTableGroupEntity(1L, Collections.emptyList());
    public static TableGroup 테이블_그룹_엔티티_B_주문_테이블_2개 = createTableGroupEntity(2L, List.of(createOrderTable(true), createOrderTable(true)));
    public static TableGroup 테이블_그룹_엔티티_C_주문_테이블_1개 = createTableGroupEntity(3L, List.of(createOrderTable(true)));

    private static TableGroup createTableGroupEntity(Long id, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    private static OrderTable createOrderTable(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(null);
        orderTable.setTableGroupId(null);
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(10);
        return orderTable;
    }
}
