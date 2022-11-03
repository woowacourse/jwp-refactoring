package kitchenpos.table.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroupResponse(Long id, LocalDateTime createdDate,
                              List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(final TableGroup tableGroup,
                                        final OrderTables savedOrderTables){
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                savedOrderTables.getOrderTables());
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
