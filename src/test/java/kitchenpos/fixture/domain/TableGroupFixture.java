package kitchenpos.fixture.domain;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup createTableGroup(final OrderTable... orderTables) {
        return new TableGroup(LocalDateTime.now(), List.of(orderTables));
    }
}
