package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<Long> orderTableIds;

    public static TableGroupResponse of(TableGroup tableGroup, List<Long> orderTableIds) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableIds);
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<Long> orderTableIds) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableIds = orderTableIds;
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
