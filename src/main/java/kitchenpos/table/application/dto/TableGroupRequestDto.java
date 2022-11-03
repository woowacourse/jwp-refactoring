package kitchenpos.table.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.presentation.dto.OrderTableIdRequest;

public class TableGroupRequestDto {

    private LocalDateTime createdDate;
    private List<OrderTableIdRequest> orderTables;

    public TableGroupRequestDto(LocalDateTime createdDate, List<OrderTableIdRequest> orderTableRequestDtos) {
        this.createdDate = createdDate;
        this.orderTables = orderTableRequestDtos;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableIdRequest> getOrderTable() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }
}
