package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {
    private static final TableGroup tableGroup = new TableGroup(0L, LocalDateTime.now(),
            Arrays.asList(OrderTableFixture.orderTable(), OrderTableFixture.orderTable()));

    public static TableGroup tableGroup() {
        return tableGroup;
    }
}
