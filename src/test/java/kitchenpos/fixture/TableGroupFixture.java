package kitchenpos.fixture;

import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;

public class TableGroupFixture {

    public static TableGroup createTableGroupWithId(Long id) {
        return new TableGroup(id, LocalDateTime.now());
    }

    public static TableGroup createTableGroupWithoutId() {
        return createTableGroupWithId(null);
    }

}
