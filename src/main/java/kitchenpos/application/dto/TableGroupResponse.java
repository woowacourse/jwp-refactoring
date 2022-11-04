package kitchenpos.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroupResponse(TableGroup tableGroup) {
        this(tableGroup.getId(), tableGroup.getCreatedDate(), tableGroup.getOrderTables());
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
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
