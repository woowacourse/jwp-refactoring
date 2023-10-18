package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.domain.common.CreatedTimeEntity;
import kitchenpos.domain.exception.InvalidEmptyOrderTableException;
import kitchenpos.domain.exception.InvalidOrderTableSizeException;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup extends CreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);

        this.orderTables = orderTables;

        groupOrderTables(orderTables);
    }

    private void groupOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.group(this);
        }
    }

    public void ungroupOrderTables() {
        validateEmptyOrderTable(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        validateOrderTableSize(orderTables);
        validateEmptyOrderTable(orderTables);
    }

    private void validateOrderTableSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new InvalidOrderTableSizeException();
        }
    }

    private void validateEmptyOrderTable(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty()) {
                throw new InvalidEmptyOrderTableException();
            }
        }
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    @Override
    public boolean equals(final Object target) {
        if (this == target) {
            return true;
        }
        if (target == null || getClass() != target.getClass()) {
            return false;
        }
        final TableGroup targetTableGroup = (TableGroup) target;
        return Objects.equals(getId(), targetTableGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
