package kitchenpos.ordertable.domain;

import static java.util.stream.Collectors.toSet;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.exception.ExceptionType.ALREADY_ASSIGNED_TABLE_GROUP;
import static kitchenpos.exception.ExceptionType.DUPLICATED_TABLES_OF_TABLE_GROUP;
import static kitchenpos.exception.ExceptionType.INVALID_TABLES_COUNT_OF_TABLE_GROUP;
import static kitchenpos.exception.ExceptionType.NOT_EMPTY_ORDER_TABLE_IN_TABLE_GROUP;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.exception.CustomException;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
        for (OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(id);
        }
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTablesDistinct(orderTables);
        validateOrderTablesHaveNoGroup(orderTables);
        validateOrderTablesEmpty(orderTables);
    }

    private void validateOrderTablesSize(List<OrderTable> orderTables) {
        if (orderTables == null || orderTables.size() < 2) {
            throw new CustomException(INVALID_TABLES_COUNT_OF_TABLE_GROUP);
        }
    }

    private void validateOrderTablesDistinct(List<OrderTable> orderTables) {
        Set<Long> uniqueOrderTableIds = orderTables.stream()
                                                   .map(OrderTable::getId)
                                                   .collect(toSet());

        if (orderTables.size() != uniqueOrderTableIds.size()) {
            throw new CustomException(DUPLICATED_TABLES_OF_TABLE_GROUP);
        }
    }

    private void validateOrderTablesHaveNoGroup(List<OrderTable> orderTables) {
        if (orderTables.stream().anyMatch(orderTable -> orderTable.getTableGroupId() != null)) {
            throw new CustomException(ALREADY_ASSIGNED_TABLE_GROUP);
        }
    }

    private void validateOrderTablesEmpty(List<OrderTable> orderTables) {
        if (orderTables.stream().anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new CustomException(NOT_EMPTY_ORDER_TABLE_IN_TABLE_GROUP);
        }
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
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

    public static class Builder {

        private Long id;
        private LocalDateTime createdDate;
        private List<OrderTable> orderTables;

        public Builder() {
        }

        public Builder(TableGroup tableGroup) {
            this.id = tableGroup.id;
            this.createdDate = tableGroup.createdDate;
            this.orderTables = tableGroup.orderTables;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder orderTables(List<OrderTable> orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public TableGroup build() {
            return new TableGroup(id, createdDate, orderTables);
        }
    }
}
