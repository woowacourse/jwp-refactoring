package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;

public class TableGroup {
    private final Long id;
    private final LocalDateTime createdDate;
    private OrderTables orderTables;

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, null);
    }

    private TableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(null, LocalDateTime.now(), new OrderTables(orderTables));
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

    public void setOrderTables(List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
    }
}
