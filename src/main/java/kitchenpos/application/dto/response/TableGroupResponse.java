package kitchenpos.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {
    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableResponse> orderTables;

    public TableGroupResponse(final Long id, final LocalDateTime createdDate,
                              final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        final List<OrderTableResponse> orderTables = mapToOrderTables(tableGroup);
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTables);
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

    private static List<OrderTableResponse> mapToOrderTables(final TableGroup tableGroup) {
        return tableGroup.getOrderTables().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }
}
