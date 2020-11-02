package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.builder.TableGroupBuilder;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupBuilder builder() {
        return new TableGroupBuilder();
    }

    public TableGroupBuilder toBuilder() {
        return new TableGroupBuilder(id, createdDate, orderTables);
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
