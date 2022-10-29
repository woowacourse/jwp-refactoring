package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.util.CollectionUtils;

public class TableGroup {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    private TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        if (id == null) {
            validateOrderTable(orderTables);
        }
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(final List<OrderTable> orderTables, final LocalDateTime localDateTime) {
        this(null, localDateTime, orderTables);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this(id, createdDate, null);
    }

    private void validateOrderTable(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public TableGroup addTableGroups(final List<OrderTable> orderTables) {
        return new TableGroup(id, createdDate, orderTables);
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
