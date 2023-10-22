package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private Long id;

    private LocalDateTime localDateTime;

    private List<OrderTableResponse> orderTableResponses;

    public TableGroupResponse(final Long id,
                              final LocalDateTime localDateTime,
                              final List<OrderTableResponse> orderTableResponses) {
        this.id = id;
        this.localDateTime = localDateTime;
        this.orderTableResponses = orderTableResponses;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        final List<OrderTableResponse> orderTableResponses = orderTables.stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toUnmodifiableList());

        return new TableGroupResponse(tableGroup.getId(),
            tableGroup.getCreatedDate(),
            orderTableResponses);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}
