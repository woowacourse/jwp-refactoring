package kitchenpos.ui.dto.tablegroup;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.ordertable.OrderTableResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup, List<OrderTable> orderTables) {
        if (Objects.isNull(tableGroup)) {
            return null;
        }
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                orderTables.stream()
                        .map(OrderTableResponse::from)
                        .collect(Collectors.toList())
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
