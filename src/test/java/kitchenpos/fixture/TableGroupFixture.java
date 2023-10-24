package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    public static TableGroup 단체_지정_생성(final List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        orderTables.forEach(orderTable -> orderTable.updateTableGroup(tableGroup));

        return tableGroup;
    }
}
