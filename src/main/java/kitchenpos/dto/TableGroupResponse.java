package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroupResponse() {
    }

    private TableGroupResponse(Long id, LocalDateTime createdDate,
        List<OrderTable> savedOrderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = savedOrderTables;
    }

    public static TableGroupResponse of(TableGroup savedTableGroup,
        List<OrderTable> savedOrderTables) {
        return new TableGroupResponse(savedTableGroup.getId(), savedTableGroup.getCreatedDate(),
            savedOrderTables);
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
