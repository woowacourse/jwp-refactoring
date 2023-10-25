package kitchenpos.fixture;

import kitchenpos.domain.tablegroup.TableGroup;

import java.time.LocalDateTime;

public class TableGroupFixture {

    public static TableGroup tableGroup() {
        return new TableGroup(LocalDateTime.now());
    }

    public static TableGroup tableGroupWithoutOrderTable(LocalDateTime createdDate) {
        return new TableGroup(createdDate);
    }
}
