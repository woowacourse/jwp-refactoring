package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableGroup {
    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new ArrayList<>(orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, new ArrayList<>());
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return new ArrayList<>(orderTables);
    }
}
