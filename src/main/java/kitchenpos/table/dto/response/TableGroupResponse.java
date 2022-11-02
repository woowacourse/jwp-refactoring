package kitchenpos.table.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableDto;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableDto> orderTables;

    private TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<OrderTableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                OrderTableDto.from(tableGroup.getOrderTables())
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
