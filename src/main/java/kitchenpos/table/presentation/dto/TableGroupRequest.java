package kitchenpos.table.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.application.dto.TableGroupRequestDto;
import kitchenpos.table.domain.OrderTable;

public class TableGroupRequest {

    private LocalDateTime createdDate;
    private List<OrderTableIdRequest> orderTables;

    public TableGroupRequest(LocalDateTime createdDate, List<OrderTableIdRequest> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public TableGroupRequestDto toServiceDto() {
        return new TableGroupRequestDto(createdDate, orderTables);
    }
}
