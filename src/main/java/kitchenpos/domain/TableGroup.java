package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

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

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
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
