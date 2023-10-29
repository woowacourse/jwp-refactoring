package kitchenpos.fixture;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    public static TableGroup createTableGroupEntity(Long id, List<OrderTable> orderTables) {
        return TableGroup.builder()
                .orderTables(orderTables)
                .build();
    }
}
