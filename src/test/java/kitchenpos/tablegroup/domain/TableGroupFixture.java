package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;

public class TableGroupFixture {

    public static TableGroup 테이블_그룹(final Long id, final LocalDateTime createdDate) {
        return new TableGroup(id, createdDate);
    }
}
