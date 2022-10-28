package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {
    
    private Long id;
    private LocalDateTime createdDate;
    private OrderTables orderTables;

    public TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
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
        return orderTables.orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = new OrderTables(orderTables);
    }

    private static class OrderTables {

        private final List<OrderTable> orderTables;

        private OrderTables(final List<OrderTable> orderTables) {
            validateLeastSize(orderTables);
            this.orderTables = orderTables;
        }

        private void validateLeastSize(final List<OrderTable> orderTables) {
            if (orderTables.isEmpty() || orderTables.size() < 2) {
                throw new IllegalArgumentException();
            }
        }
    }
}
