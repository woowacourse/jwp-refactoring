package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.util.CollectionUtils;

public class TableGroup {

    private static final int MINIMUM_ORDER_TABLE_SIZE = 2;

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
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

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup initCreatedDate() {
        return new TableGroup(id, LocalDateTime.now(), orderTables);
    }

    public boolean hasValidOrderTableSize() {
        return !CollectionUtils.isEmpty(orderTables) && orderTables.size() >= MINIMUM_ORDER_TABLE_SIZE;
    }
}
