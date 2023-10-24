package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.util.CollectionUtils;

public class TableGroup {

    private static final int MINIMUM_ORDER_TABLES_SIZE = 2;
    private Long id;
    private LocalDateTime createdDate;
    private final List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {

    }

    public TableGroup(final Long id) {
        this.id = id;
    }

    public void addOrderTables(final List<OrderTable> orderTables) {
        validateOrderTableSize(orderTables);
        for (final OrderTable orderTable : orderTables) {
            addOrderTable(orderTable);
        }
    }

    private void validateOrderTableSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_ORDER_TABLES_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    private void addOrderTable(final OrderTable orderTable) {
        orderTable.belongsToTableGroupId(id);
        orderTable.changeEmpty(OrderTable.NOT_EMPTY);
        this.orderTables.add(orderTable);
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return new ArrayList<>(orderTables);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TableGroup tableGroup = (TableGroup) o;
        if (Objects.isNull(this.id) || Objects.isNull(tableGroup.id)) {
            return false;
        }
        return Objects.equals(id, tableGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
