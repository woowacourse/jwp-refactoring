package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {

    private final Long id;
    private final LocalDateTime createdDate;
    private OrderTables orderTables;

    /**
     * DB 에 저장되지 않은 객체
     */
    public TableGroup(final List<OrderTable> orderTables) {
        this(null, null, orderTables);
    }

    /**
     * DB 에 저장된 객체
     */
    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new OrderTables(orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getValue();
    }
}
