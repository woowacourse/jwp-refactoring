package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public abstract class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup tableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        return new TableGroup(createdDate, orderTables);
    }
}
