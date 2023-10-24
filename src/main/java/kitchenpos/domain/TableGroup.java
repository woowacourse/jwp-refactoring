package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {

    private static final int MIN_TABLE_SIZE = 2;

    private Long id;
    private LocalDateTime createdDate;
    private OrderTables orderTables;

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, null);
    }

    private TableGroup(LocalDateTime createdDate, OrderTables orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup from(OrderTables orderTables) {
        orderTables.validateTableStatus();
        if (orderTables.size() < MIN_TABLE_SIZE) {
            throw new IllegalArgumentException("2개 미만의 테이블은 단체로 지정할 수 없습니다.");
        }
        return new TableGroup(LocalDateTime.now(), orderTables);
    }

    public void assignTables(OrderTables orderTables) {
        this.orderTables = orderTables;
        orderTables.group(this.id);
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
        return orderTables.getOrderTables();
    }

    public void setOrderTables(final OrderTables orderTables) {
        this.orderTables = orderTables;
    }
}
