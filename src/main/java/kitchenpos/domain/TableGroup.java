package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableGroup {
    private final Long id;
    private final LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new ArrayList<>(orderTables);
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public void addOrderTable(final OrderTable... orderTables) {
        this.orderTables.addAll(Arrays.asList(orderTables));
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

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
