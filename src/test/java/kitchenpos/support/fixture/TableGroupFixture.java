package kitchenpos.support.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup create(OrderTable... orderTables) {
        return new TableGroup(LocalDateTime.now(), Arrays.asList(orderTables));
    }
}
