package kitchenpos.domain.fixture;

import kitchenpos.table.TableGroup;

import java.time.LocalDateTime;

public abstract class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup tableGroup(final LocalDateTime createdDate) {
        return new TableGroup(createdDate);
    }
}
