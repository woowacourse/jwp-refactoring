package kitchenpos.table_group.application.dto.response;

import kitchenpos.table.application.dto.response.OrderTableQueryResponse;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table_group.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupQueryResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableQueryResponse> orderTables;

    public TableGroupQueryResponse(final Long id, final LocalDateTime createdDate,
                                   final List<OrderTableQueryResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroupQueryResponse() {
    }

    public static TableGroupQueryResponse of(final TableGroup tableGroup, final OrderTables orderTables) {
        return new TableGroupQueryResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
                OrderTableQueryResponse.from(orderTables));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableQueryResponse> getOrderTables() {
        return orderTables;
    }
}
