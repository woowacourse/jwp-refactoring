package kitchenpos.tablegroup.application.dto.response;

import kitchenpos.ordertable.application.dto.response.OrderTableInTableGroupResponse;
import kitchenpos.tablegroup.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableInTableGroupResponse> orderTables;

    public TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<OrderTableInTableGroupResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                tableGroup.getOrderTables().stream()
                        .map(OrderTableInTableGroupResponse::from)
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableInTableGroupResponse> getOrderTables() {
        return orderTables;
    }
}
