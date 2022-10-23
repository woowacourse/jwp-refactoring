package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableGroup {

    private Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    public TableGroup(LocalDateTime createdDate) {
        this(null, createdDate, new ArrayList<>());
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, new ArrayList<>());
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void addOrderTables(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }
}
