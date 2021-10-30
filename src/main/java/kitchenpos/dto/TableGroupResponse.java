package kitchenpos.dto;

import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime localDateTime;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse(Long id, LocalDateTime localDateTime, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.localDateTime = localDateTime;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                tableGroup.getOrderTables().stream()
                        .map(OrderTableResponse::from)
                        .collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
