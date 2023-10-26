package kitchenpos.domain.order.service.dto;

import kitchenpos.domain.table.TableGroup;

import java.time.LocalDateTime;

public class TableGroupResponse {

    private final Long id;

    private final LocalDateTime createdDate;

    public TableGroupResponse(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroupResponse toDto(final TableGroup savedTableGroup) {
        return new TableGroupResponse(savedTableGroup.getId(), savedTableGroup.getCreatedDate());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
