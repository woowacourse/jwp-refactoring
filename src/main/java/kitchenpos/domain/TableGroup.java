package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.exception.AlreadyGroupedException;

public class TableGroup {
    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, List.of());
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            validateOrderTable(orderTable);
        }
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isNotPossibleTableGrouping()) {
            throw new AlreadyGroupedException();
        }
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
}
