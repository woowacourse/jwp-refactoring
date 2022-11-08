package kitchenpos.ordertable.ui.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), tableGroup.getOrderTables());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
