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
        public static TableGroup 테이블_그룹() {
            TableGroup tableGroup = TableGroup.builder()
                    .id(1L)
                    .build();
            OrderTable orderTable = ORDER_TABLE.주문_테이블_1();
            OrderTable orderTable2 = ORDER_TABLE.주문_테이블_2();
            return tableGroup.updateOrderTables(List.of(orderTable.ungroup(), orderTable2.ungroup()));
        }
    }
}
