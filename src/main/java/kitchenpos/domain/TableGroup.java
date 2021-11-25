package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroup {
    private final Long id;
    private final LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup(LocalDateTime localDateTime, List<OrderTable> savedOrderTables) {
        this(null, localDateTime, savedOrderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
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

    public List<Long> getTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void setTableGroupIdInOrderTables(Long tableGroupId) {
        this.orderTables = orderTables.stream()
                .map(it -> new OrderTable(it.getId(), tableGroupId, it.getNumberOfGuests(), it.isEmpty()))
                .collect(Collectors.toList());
    }

    public void unGrouping() {
        this.orderTables = orderTables.stream()
                .map(it -> new OrderTable(it.getId(), null, it.getNumberOfGuests(), false))
                .collect(Collectors.toList());
    }
}
