package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroup implements Entity {
    
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this(id, createdDate, new ArrayList<>());
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
        if (isNew()) {
            validateOnCreate();
        }
    }

    public void ungroup() {
        validateCanBeUngrouped();
        this.orderTables = ungroupAllTables();;
    }

    private void validateCanBeUngrouped() {
        final var isAllTablesCanBeUngrouped = orderTables
                .stream()
                .allMatch(OrderTable::canBeUngrouped);

        if (!isAllTablesCanBeUngrouped) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> ungroupAllTables() {
        return orderTables
                .stream()
                .map(orderTable -> new OrderTable(
                        orderTable.getId(),
                        null,
                        orderTable.getNumberOfGuests(),
                        false,
                        orderTable.getOrders())
                ).collect(Collectors.toList());
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public void validateOnCreate() {
        validateAllTablesCanBeGrouped();
        validateTablesNotEmptyAndLeastSizeTwo();
    }

    private void validateAllTablesCanBeGrouped() {
        final var canAllTablesBeGrouped = orderTables.stream()
                .allMatch(OrderTable::canBeGrouped);
        if (!canAllTablesBeGrouped) {
            throw new IllegalArgumentException();
        }
    }

    private void validateTablesNotEmptyAndLeastSizeTwo() {
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
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

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
