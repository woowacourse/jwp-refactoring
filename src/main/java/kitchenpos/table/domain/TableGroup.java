package kitchenpos.table.domain;

import java.time.LocalDateTime;
import kitchenpos.common.domain.BaseDomain;

public class TableGroup extends BaseDomain {

    private final Long id;

    public TableGroup(Long id, LocalDateTime createdDate) {
        super(createdDate);
        this.id = id;
    }

    public TableGroup(LocalDateTime createdTime) {
        this(null, createdTime);
    }

    public static TableGroup from() {
        return new TableGroup(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }
}
