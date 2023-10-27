package kitchenpos.tablegroup.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.ordertable.application.dto.OrderTableResponse;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    private TableGroupResponse(final Long id, final LocalDateTime createdDate,
                              final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    @JsonCreator
    public static TableGroupResponse from(final TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                OrderTableResponse.from(tableGroup.getOrderTables())
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
