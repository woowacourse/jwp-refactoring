package kitchenpos.fixture;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static class TABLE_GROUP {
        public static TableGroup 테이블_그룹_1() {
            TableGroup tableGroup = new TableGroup();
            tableGroup.setId(1L);
            OrderTable orderTable = ORDER_TABLE.주문_테이블_1();
            OrderTable orderTable2 = ORDER_TABLE.주문_테이블_2();
            orderTable.setTableGroupId(null);
            orderTable2.setTableGroupId(null);
            tableGroup.setOrderTables(List.of(orderTable, orderTable2));
            return tableGroup;
        }
    }
}
