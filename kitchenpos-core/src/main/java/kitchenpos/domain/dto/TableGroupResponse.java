package kitchenpos.domain.dto;

import kitchenpos.domain.table.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {

    private final Long id;
    private final List<Long> orderTableIds;
    private final LocalDateTime createdDate;

    public TableGroupResponse(final Long id, final List<Long> orderTableIds, final LocalDateTime createdDate) {
        this.id = id;
        this.orderTableIds = orderTableIds;
        this.createdDate = createdDate;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getOrderTables().getOrderTableIds(),
                tableGroup.getCreatedDate()
        );
    }

    public Long getId() {
        return id;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
