package kitchenpos.application.response;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.model.tablegroup.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<Long> orderTables;

    private TableGroupResponse() {
    }

    private TableGroupResponse(Long id, LocalDateTime createdDate,
            List<Long> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
                tableGroup.orderTableIds());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
