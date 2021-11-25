package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.table.exception.InvalidTableGroupException;
import kitchenpos.table.exception.InvalidTableGroupSizeException;

@Entity
public class TableGroup {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new InvalidTableGroupSizeException();
        }

        if (orderTables.stream().anyMatch(orderTable -> !orderTable.canCreateTableGroup())) {
            throw new InvalidTableGroupException();
        }
    }

    public void setTables() {
        this.orderTables.forEach(orderTable -> orderTable.includeInTableGroup(this.id));
    }

    public void ungroup() {
        this.orderTables.forEach(OrderTable::excludeFromTableGroup);
    }

    public List<Long> orderTableIds() {
        return this.orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
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
