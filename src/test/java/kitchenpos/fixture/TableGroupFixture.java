package kitchenpos.fixture;

import java.time.LocalDateTime;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup from(LocalDateTime createdDate) {
        return new TableGroup(createdDate);
    }
}