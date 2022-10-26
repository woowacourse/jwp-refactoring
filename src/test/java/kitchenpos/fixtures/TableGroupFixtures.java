package kitchenpos.fixtures;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixtures {

    public static TableGroup create() {
        return new TableGroup(null, LocalDateTime.now());
    }

    public static TableGroup createWithOrderTables(final OrderTable... orderTables) {
        return new TableGroup(null, LocalDateTime.now(), Arrays.asList(orderTables));
    }
}
