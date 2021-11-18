package kitchenpos.ui.dto.tablegroup;

import kitchenpos.domain.order.TableGroup;
import kitchenpos.ui.dto.order.OrderTableRequest;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableRequest> orderTables;

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableRequest> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                OrderTableRequest.toList(tableGroup.getOrderTables()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
