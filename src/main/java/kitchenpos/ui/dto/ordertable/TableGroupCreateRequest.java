package kitchenpos.ui.dto.ordertable;

import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public TableGroup toEntity() {
        return new TableGroup(
                null,
                null,
                new OrderTables(orderTables.stream().map(OrderTableRequest::toEntity).collect(Collectors.toList()))
        );
    }
}
