package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    private static final int ORDER_TABLE_MIN_SIZE = 2;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
        orderTables.forEach(orderTable -> orderTable.changeEmpty(false));
    }

    protected OrderTables() {
    }

    private void validate(final List<OrderTable> orderTables) {
        validateOrderTablesIsExist(orderTables);
        validateOrderTablesSize(orderTables);
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < ORDER_TABLE_MIN_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesIsExist(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
