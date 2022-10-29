package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {
    private final Long id;
    private final LocalDateTime createdDate;
    private OrderTables orderTables;

    public TableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, null);
    }

    public TableGroup(List<OrderTable> orderTables) {
        this(null, null, new OrderTables(orderTables));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
    }
}
