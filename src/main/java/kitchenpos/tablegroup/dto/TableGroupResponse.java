package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;

    public TableGroupResponse(
            final Long id,
            final LocalDateTime createdDate
    ) {
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
