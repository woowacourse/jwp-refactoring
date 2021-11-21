package kitchenpos.tablegroup.application.dto;

import java.time.LocalDateTime;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {
    private final Long id;
    private final LocalDateTime createdDate;

    public TableGroupResponse(TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.createdDate = tableGroup.getCreatedDate();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

}
