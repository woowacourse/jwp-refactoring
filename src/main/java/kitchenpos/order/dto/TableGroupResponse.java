package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup, List<OrderTable> orderTables) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
