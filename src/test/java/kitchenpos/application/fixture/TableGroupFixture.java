package kitchenpos.application.fixture;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup createTableGroup(final Long id, final List<OrderTable> orderTables) {
        return new TableGroup(id, LocalDateTime.now(), orderTables);
    }

    public static TableGroup createTableGroup(final Long id) {
        return createTableGroup(id, Collections.emptyList());
    }
}
