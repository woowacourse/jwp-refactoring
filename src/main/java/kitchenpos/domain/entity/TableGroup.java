package kitchenpos.domain.entity;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.service.TableGroupCreateService;

public class TableGroup {
    private final Long id;
    private final List<OrderTable> orderTables;
    private LocalDateTime createdDate;

    public TableGroup(Long id, List<OrderTable> orderTables, LocalDateTime createdDate) {
        this.id = id;
        this.orderTables = orderTables;
        this.createdDate = createdDate;
    }

    public TableGroup create(TableGroupCreateService tableGroupCreateService) {
        tableGroupCreateService.validate(orderTableIds());
        this.createdDate = LocalDateTime.now();
        return this;
    }

    public List<Long> orderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
