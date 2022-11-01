package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private OrderTables orderTables;

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this(id, createdDate, new OrderTables(Collections.emptyList()));
    }

    public TableGroup(final OrderTables orderTables) {
        this(null, LocalDateTime.now(), orderTables);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final OrderTables orderTables) {
        orderTables.validateGroupSize();
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
    }
}
