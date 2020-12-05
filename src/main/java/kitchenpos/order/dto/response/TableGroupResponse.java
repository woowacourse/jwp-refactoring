package kitchenpos.order.dto.response;

import kitchenpos.order.domain.TableGroup;

import java.time.LocalDateTime;

public class TableGroupResponse {
    private final Long id;
    private final LocalDateTime createdDate;

    public TableGroupResponse(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
