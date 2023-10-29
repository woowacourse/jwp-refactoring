package kitchenpos.tablegroup.application.dto.response;

import kitchenpos.tablegroup.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<Long> orderTableIds;

    public TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<Long> orderTableIds) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupResponse of(final TableGroup tableGroup, final List<Long> orderTableIds) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                orderTableIds
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
