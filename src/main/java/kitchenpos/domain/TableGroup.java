package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableGroup {

    private final Long id;
    private LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new ArrayList<>();
    }

    public void addOrderTables(final List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public void setCreatedDateNow() {
        this.createdDate = LocalDateTime.now();
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
