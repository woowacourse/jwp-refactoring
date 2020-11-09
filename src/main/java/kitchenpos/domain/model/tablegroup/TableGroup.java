package kitchenpos.domain.model.tablegroup;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.model.AggregateReference;
import kitchenpos.domain.model.ordertable.OrderTable;

public class TableGroup {
    private final Long id;
    private final List<AggregateReference<OrderTable>> orderTables;
    private LocalDateTime createdDate;

    public TableGroup(Long id, List<AggregateReference<OrderTable>> orderTables, LocalDateTime createdDate) {
        this.id = id;
        this.orderTables = orderTables;
        this.createdDate = createdDate;
    }

    public TableGroup create(TableGroupCreateService tableGroupCreateService) {
        tableGroupCreateService.validate(orderTableIds());
        this.createdDate = LocalDateTime.now();
        return this;
    }

    public void ungroup(TableGroupUngroupService tableGroupUngroupService) {
        tableGroupUngroupService.resetOrderTables(orderTableIds());
    }

    public List<Long> orderTableIds() {
        return orderTables.stream()
                .map(AggregateReference::getId)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public List<AggregateReference<OrderTable>> getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
