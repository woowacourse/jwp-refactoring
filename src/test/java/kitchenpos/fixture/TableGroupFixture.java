package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup create(OrderTable... orderTables) {
        return new TableGroup(null, LocalDateTime.now(), Arrays.asList(orderTables));
    }
}
