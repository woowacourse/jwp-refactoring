package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, null);
    }

    private TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            validateOrderTable(orderTable);
        }
        return new TableGroup(LocalDateTime.now(), orderTables);
    }

    private static void validateOrderTable(OrderTable orderTable) {
        if (!orderTable.isEmpty() || orderTable.getTableGroupId() != null) {
            throw new IllegalArgumentException("비어 있지 않거나, 이미 그룹으로 지정된 테이블은 단체로 지정할 수 없습니다.");
        }
    }

    public void assignTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        for (OrderTable orderTable : this.orderTables) {
            orderTable.group(this.id);
        }
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

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
