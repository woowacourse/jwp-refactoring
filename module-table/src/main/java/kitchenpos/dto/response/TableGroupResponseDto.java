package kitchenpos.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dto.response.OrderTableResponseDto;

public class TableGroupResponseDto {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponseDto> orderTables;

    private TableGroupResponseDto() {
    }

    public TableGroupResponseDto(Long id, LocalDateTime createdDate,
        List<OrderTableResponseDto> orderTables) {
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

    public List<OrderTableResponseDto> getOrderTables() {
        return orderTables;
    }
}
