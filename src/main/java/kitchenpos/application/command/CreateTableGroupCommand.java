package kitchenpos.application.command;

import java.util.HashSet;
import java.util.List;

import javax.validation.constraints.Size;

import kitchenpos.domain.model.AggregateReference;
import kitchenpos.domain.model.ordertable.OrderTable;
import kitchenpos.domain.model.tablegroup.TableGroup;

public class CreateTableGroupCommand {
    @Size(min = 2)
    private List<AggregateReference<OrderTable>> orderTables;

    private CreateTableGroupCommand() {
    }

    public CreateTableGroupCommand(List<AggregateReference<OrderTable>> orderTables) {
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
