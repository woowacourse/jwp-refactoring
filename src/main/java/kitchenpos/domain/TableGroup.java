package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {

    private final Long id;
    private final LocalDateTime createdDate;

    public TableGroup(final LocalDateTime createdDate) {
        this(null, createdDate);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public void unite(final List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        for (OrderTable orderTable : orderTables) {
            orderTable.joinGroup(id);
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

}
