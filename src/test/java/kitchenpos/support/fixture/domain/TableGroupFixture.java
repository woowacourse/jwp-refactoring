package kitchenpos.support.fixture.domain;

import java.time.LocalDateTime;
import kitchenpos.table.domain.TableGroup;

public enum TableGroupFixture {

    NONE,
    ;

    public static TableGroup getTableGroup() {
        return TableGroup.from();
    }

    public static TableGroup getTableGroup(Long id, LocalDateTime createdDate) {
        return new TableGroup(id, createdDate);
    }
}
