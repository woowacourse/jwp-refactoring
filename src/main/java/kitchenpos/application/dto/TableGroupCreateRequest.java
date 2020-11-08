package kitchenpos.application.dto;

import java.util.HashSet;
import java.util.List;

import javax.validation.constraints.Size;

import kitchenpos.core.AggregateReference;
import kitchenpos.domain.entity.OrderTable;
import kitchenpos.domain.entity.TableGroup;

public class TableGroupCreateRequest {
    @Size(min = 2)
    private List<AggregateReference<OrderTable>> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<AggregateReference<OrderTable>> orderTables) {
        this.orderTables = orderTables;
        validate(this.orderTables);
    }

    private void validate(List<AggregateReference<OrderTable>> orderTables) {
        if (hasDuplicatedItem(orderTables)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean hasDuplicatedItem(List<AggregateReference<OrderTable>> orderTables) {
        return !orderTables.stream()
                .mapToLong(AggregateReference::getId)
                .allMatch(new HashSet<>()::add);
    }

    public TableGroup toEntity() {
        return new TableGroup(null, orderTables, null);
    }

    public List<AggregateReference<OrderTable>> getOrderTables() {
        return orderTables;
    }
}
