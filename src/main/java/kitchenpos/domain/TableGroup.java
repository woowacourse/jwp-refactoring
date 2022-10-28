package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables.addAll(orderTables);
    }

    public static TableGroup ofNullId(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        return new TableGroup(null, createdDate, orderTables);
    }

    public void addAllOrderTables(final List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public void updateCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
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
