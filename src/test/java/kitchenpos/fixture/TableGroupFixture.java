package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {
    public static TableGroup tableGroup() {
        return new TableGroup(0L, LocalDateTime.now(),
                Arrays.asList(OrderTableFixture.orderTable(), OrderTableFixture.orderTable()));
    }
}
