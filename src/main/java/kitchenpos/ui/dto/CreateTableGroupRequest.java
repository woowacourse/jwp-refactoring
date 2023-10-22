package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class CreateTableGroupRequest {

    private List<CreateTableGroupOrderTableRequest> orderTables;

    public CreateTableGroupRequest() {
    }

    public CreateTableGroupRequest(final List<CreateTableGroupOrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<CreateTableGroupOrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public TableGroup toEntity() {
        final TableGroup tableGroup = new TableGroup();
        final List<OrderTable> orderTables = this.orderTables.stream()
                                                             .map(CreateTableGroupOrderTableRequest::toEntity)
                                                             .collect(Collectors.toList());
        tableGroup.setOrderTables(orderTables);

        return tableGroup;
    }
}
