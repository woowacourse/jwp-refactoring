package kitchenpos.tableGroup.dto;

import kitchenpos.tableGroup.domain.TableGroup;

import java.time.LocalDateTime;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;

    private TableGroupResponse(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
