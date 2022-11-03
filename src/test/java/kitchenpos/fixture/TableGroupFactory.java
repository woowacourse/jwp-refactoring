package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

public class TableGroupFactory {

    public static TableGroup tableGroup(final OrderTable... orderTables) {
        return new TableGroup(null, LocalDateTime.now(), List.of(orderTables));
    }
}
