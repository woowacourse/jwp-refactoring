package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.CollectionUtils;

public class TableGroup {

    private static final int MIN_TABLES_COUNT = 2;

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    private TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, new ArrayList<>());
    }

    public TableGroup(List<OrderTable> orderTables) {
        this(null, LocalDateTime.now(), orderTables);
        validateOrderTables(orderTables);
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTablesAlreadyInTableGroup();
    }

    private void validateOrderTablesSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_TABLES_COUNT) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesAlreadyInTableGroup() {
        if (hasInvalidOrderTable()) {
            throw new IllegalArgumentException();
        }
    }

    private boolean hasInvalidOrderTable() {
        return this.orderTables
                .stream()
                .anyMatch(orderTable -> !orderTable.isEmpty() || orderTable.hasTableGroupId());
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
