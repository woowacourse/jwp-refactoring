package kitchenpos.domain.table;

import kitchenpos.exception.tableGroupException.DuplicateCreateTableGroup;
import kitchenpos.exception.orderTableException.InvalidOrderTableException;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class OrderTables {

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new InvalidOrderTableException();
        }

        for (final OrderTable savedOrderTable : orderTables) {
            if (savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new DuplicateCreateTableGroup();
            }
        }
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
        orderTables.clear();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

}
