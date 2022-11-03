package kitchenpos.table.ui.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.TableGroup;

public class TableGroupCreateResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableCreateResponse> orderTables;

    public TableGroupCreateResponse() {
    }

    public TableGroupCreateResponse(final Long id, final LocalDateTime createdDate,
                                    final List<OrderTableCreateResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupCreateResponse from(final TableGroup tableGroup) {
        return new TableGroupCreateResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
                OrderTableCreateResponse.from(tableGroup.getOrderTables()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableCreateResponse> getOrderTables() {
        return orderTables;
    }
}
