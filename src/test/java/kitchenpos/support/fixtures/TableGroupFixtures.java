package kitchenpos.support.fixtures;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupFixtures {

    public static TableGroup create() {
        return new TableGroup(null, LocalDateTime.now());
    }

    public static TableGroup createWithOrderTables(final OrderTable... orderTables) {
        return new TableGroup(null, LocalDateTime.now(), Arrays.asList(orderTables));
    }
}
