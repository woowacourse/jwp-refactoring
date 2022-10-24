package kitchenpos.support.fixture.domain;

import java.time.LocalDateTime;
import kitchenpos.domain.TableGroup;

public enum TableGroupFixture {

    NONE,
    ;

    public static TableGroup getTableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }

    public static TableGroup getTableGroup(Long id) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }
}
