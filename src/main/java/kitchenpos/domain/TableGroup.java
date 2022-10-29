package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroup {
    
    private Long id;
    private LocalDateTime createdDate;
    private OrderTables orderTables;

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        if (isNewEntity(id)) {
            validateAllTablesCanBeGrouped(orderTables);
        }
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new OrderTables(orderTables);
    }

    private boolean isNewEntity(final Long id) {
        return id == null;
    }

    private void validateAllTablesCanBeGrouped(final List<OrderTable> orderTables) {
        final var canAllTablesBeGrouped = orderTables.stream()
                .allMatch(OrderTable::canBeGrouped);
        if (!canAllTablesBeGrouped) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup() {
        validateCanBeUngrouped();
        final List<OrderTable> ungroupedOrderTables = ungroupAllTables();
        this.orderTables = new OrderTables(ungroupedOrderTables);
    }

    private void validateCanBeUngrouped() {
        final var isAllTablesCanBeUngrouped = orderTables.orderTables
                .stream()
                .allMatch(OrderTable::canBeUngrouped);

        if (!isAllTablesCanBeUngrouped) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> ungroupAllTables() {
        return orderTables.orderTables
                .stream()
                .map(orderTable -> new OrderTable(
                        orderTable.getId(),
                        null,
                        orderTable.getNumberOfGuests(),
                        false,
                        orderTable.getOrders())
                ).collect(Collectors.toList());
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
        return orderTables.orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
    }

    private static class OrderTables {

        private final List<OrderTable> orderTables;

        private OrderTables(final List<OrderTable> orderTables) {
            validateLeastSize(orderTables);
            this.orderTables = orderTables;
        }

        private void validateLeastSize(final List<OrderTable> orderTables) {
            if (orderTables.isEmpty() || orderTables.size() < 2) {
                throw new IllegalArgumentException();
            }
        }
    }
}
