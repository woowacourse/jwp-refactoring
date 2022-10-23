package kitchenpos.support;

import java.time.LocalDateTime;
import kitchenpos.domain.TableGroup;

public enum TableGroupFixture {

    TABLE_GROUP_NOW(LocalDateTime.now());

    private final LocalDateTime localDateTime;

    TableGroupFixture(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public TableGroup 생성() {
        return new TableGroup(this.localDateTime);
    }
}
