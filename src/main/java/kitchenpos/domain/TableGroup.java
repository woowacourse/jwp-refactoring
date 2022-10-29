package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private OrderTables orderTables;

    public TableGroup(List<OrderTable> orderTables) {
        this.createdDate = LocalDateTime.now();
        this.orderTables = new OrderTables(orderTables);
    }

    public TableGroup() {
    }

    public void checkOrderTableSize(int orderTableSize) {
        if (orderTables.equalSize(orderTableSize)) {
            throw new IllegalArgumentException("주문 테이블의 수가 맞지 않습니다.");
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
        return orderTables.getOrderTables();
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
    }
}
