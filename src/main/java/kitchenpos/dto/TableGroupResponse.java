package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    private TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTableResponses) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTableResponses;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(
            tableGroup.getId(),
            tableGroup.getCreatedDate(),
            OrderTableResponse.ofList(tableGroup.getOrderTables())
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
