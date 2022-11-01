package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {
    private final Long id;
    private final LocalDateTime createdDate;
    private OrderTables orderTables;

    public TableGroup(final Long id, final LocalDateTime createdDate, final OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(id, createdDate, new OrderTables(orderTables));
    }

    public TableGroup(final LocalDateTime createdDate, final OrderTables orderTables) {
        this(null, createdDate, orderTables);
    }

    public void changeAllOrderTables(final OrderTables orderTables) {
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public List<OrderTable> getAllOrderTables() {
        return orderTables.getOrderTables();
    }
}
