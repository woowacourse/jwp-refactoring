package kitchenpos.dto.tablegroup.response;

import java.time.LocalDateTime;
import kitchenpos.domain.tablegroup.TableGroup;

public class TableGroupResponse {
    private final Long id;
    private final LocalDateTime createdDate;

    public TableGroupResponse(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.id(),
                tableGroup.createdDate()
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
