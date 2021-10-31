package kitchenpos.dto.tablegroup;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.ordertable.OrderTableResponse;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableResponse> orderTables;

    public TableGroupResponse(TableGroup tableGroup, List<OrderTableResponse> orderTableResponses) {
        this(
            tableGroup.getId(),
            tableGroup.getCreatedDate(),
            orderTableResponses
        );
    }

    public TableGroupResponse(
        Long id,
        LocalDateTime createdDate,
        List<OrderTableResponse> orderTables
    ) {
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

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
