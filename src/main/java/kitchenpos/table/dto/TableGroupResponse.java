package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.TableGroup;

public class TableGroupResponse {
    private final Long id;

    private final LocalDateTime createdDate;

    private final List<OrderTableResponse> orderTableResponses;

    public TableGroupResponse(
            Long id,
            LocalDateTime createdDate,
            List<OrderTableResponse> orderTableResponses
    ) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableResponses = orderTableResponses;
    }

    public TableGroupResponse(TableGroup tableGroup) {
        this(tableGroup.getId()
                ,tableGroup.getCreatedDate()
                ,tableGroup.getOrderTables()
                        .stream()
                        .map(OrderTableResponse::new)
                        .collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}
