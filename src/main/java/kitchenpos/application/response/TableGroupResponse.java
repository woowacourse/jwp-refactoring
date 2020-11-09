package kitchenpos.application.response;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.model.AggregateReference;
import kitchenpos.domain.model.ordertable.OrderTable;
import kitchenpos.domain.model.tablegroup.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<AggregateReference<OrderTable>> orderTables;

    private TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate,
            List<AggregateReference<OrderTable>> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
                tableGroup.getOrderTables());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<AggregateReference<OrderTable>> getOrderTables() {
        return orderTables;
    }
}
