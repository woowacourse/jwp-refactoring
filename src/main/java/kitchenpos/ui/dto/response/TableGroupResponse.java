package kitchenpos.ui.dto.response;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    private TableGroupResponse() {
    }

    private TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse create(TableGroup tableGroup, List<OrderTable> orderTables) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), OrderTableResponse.of(orderTables));
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
