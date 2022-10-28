package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {
    private final Long id;
    private final LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroup ofNew() {
        return new TableGroup(null, LocalDateTime.now());
    }

    public List<OrderTable> groupTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.joinGroup(id);
        }

        this.orderTables = orderTables;
        return orderTables;
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
