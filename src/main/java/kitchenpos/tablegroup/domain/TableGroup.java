package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;

public class TableGroup {

    private final Long id;
    private final LocalDateTime createdDate;

    public TableGroup() {
        this(null, LocalDateTime.now());
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
