package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.CollectionUtils;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this(id, createdDate, new ArrayList<>());
    }

    public void validateExistOrderTable(final long existOrderTableSize) {
        if (orderTables.size() != existOrderTableSize) {
            throw new IllegalArgumentException();
        }
    }

    public void updateOrderTables(final List<OrderTable> groupedOrderTables) {
        validateSizeOfOrderTables(groupedOrderTables);
        this.orderTables = new ArrayList<>(groupedOrderTables);
    }

    private void validateSizeOfOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
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
