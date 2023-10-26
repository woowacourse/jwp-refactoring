package kitchenpos.table_group.domain;

import java.time.LocalDateTime;

public class TableGroup {

    private final Long id;
    private final LocalDateTime createdDate;

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup(final LocalDateTime createdDate) {
        this(null, createdDate);
    }


    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
