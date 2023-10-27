package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.OrderTable;
import kitchenpos.tablegroup.TableGroup;

public class TableGroupFixture {

    public static TableGroup 테이블_그룹(final Long id, final LocalDateTime createdDate,
                                    final List<OrderTable> orderTables) {
        return new TableGroup(id, createdDate, orderTables);
    }
}
