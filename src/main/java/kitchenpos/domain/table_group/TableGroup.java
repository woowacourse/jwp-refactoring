package kitchenpos.domain.table_group;

import static kitchenpos.exception.TableGroupException.HasAlreadyGroupedTableException;
import static kitchenpos.exception.TableGroupException.HasEmptyTableException;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.TableGroupException.NoMinimumOrderTableSizeException;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @OneToMany(mappedBy = "tableGroup")
    private final List<OrderTable> orderTables;
    private final LocalDateTime createdDate;

    public TableGroup() {
        this.id = null;
        this.orderTables = null;
        this.createdDate = null;
    }

    public TableGroup(final List<OrderTable> orderTables) {
        id = null;
        validateSize(orderTables);
        validateContainEmptyTable(orderTables);
        validateContainOtherTableGroup(orderTables);
        this.orderTables = List.copyOf(orderTables);
        this.createdDate = LocalDateTime.now();
    }

    private void validateSize(final List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new NoMinimumOrderTableSizeException();
        }
    }

    private void validateContainEmptyTable(final List<OrderTable> orderTables) {
        orderTables.forEach(this::validateContainEmptyTable);
    }

    private void validateContainEmptyTable(final OrderTable orderTables) {
        if (orderTables.isEmpty()) {
            throw new HasEmptyTableException();
        }
    }

    private void validateContainOtherTableGroup(final List<OrderTable> orderTables) {
        orderTables.forEach(this::validateContainOtherTableGroup);
    }

    private void validateContainOtherTableGroup(final OrderTable orderTable) {
        if (orderTable.isGrouped()) {
            throw new HasAlreadyGroupedTableException();
        }
    }

    public void group(final OrderTable orderTable) {
        orderTable.group(this);
        orderTables.add(orderTable);
    }

    public void upGroup() {
        orderTables.forEach(OrderTable::unGroup);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
