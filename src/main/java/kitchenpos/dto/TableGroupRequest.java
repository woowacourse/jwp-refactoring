package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupRequest {

    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroupRequest(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup toEntity() {
        return new TableGroup(createdDate, orderTables);
    }

    public TableGroup toEntity(Long id) {
        TableGroup tableGroup = new TableGroup(createdDate, orderTables);
        tableGroup.setId(id);
        return tableGroup;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
