package kitchenpos.domain.tablegroup;

import kitchenpos.domain.order.OrderTable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroup of(List<OrderTable> orderTables, int requestSize) {
        validateOrderTables(orderTables, requestSize);
        return new TableGroup(null, LocalDateTime.now(), new ArrayList<>(orderTables));
    }

    private static void validateOrderTables(List<OrderTable> orderTables, int requestSize) {
        validateSize(orderTables, requestSize);
        validateEmptyOrNull(orderTables);
    }

    private static void validateSize(List<OrderTable> orderTables, int requestSize) {
        if (orderTables.size() != requestSize) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateEmptyOrNull(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
