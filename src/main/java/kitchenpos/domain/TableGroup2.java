package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup2 {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable2> orderTables;

    public TableGroup2(
        final Long id,
        final LocalDateTime createdDate,
        final List<OrderTable2> orderTables
    ) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup2(final LocalDateTime createdDate, final List<OrderTable2> orderTables) {
        this(null, createdDate, orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable2> getOrderTables() {
        return orderTables;
    }
}
