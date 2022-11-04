package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    @OneToMany
    @JoinColumn(name = "order_table_id")
    private List<OrderTable> values;

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> values) {
        validate(values);
        this.values = values;
    }

    private void validate(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTables group(final TableGroup savedTableGroup) {
        List<OrderTable> groupedOrderTables = new ArrayList<>();
        for (final OrderTable orderTable : values) {
            validateOrderTableIsAbleToGroup(orderTable);
            OrderTable validatedOrderTable = new OrderTable(orderTable.getId(), savedTableGroup,
                    orderTable.getNumberOfGuests(), false);
            groupedOrderTables.add(validatedOrderTable);
        }
        return new OrderTables(groupedOrderTables);
    }

    private void validateOrderTableIsAbleToGroup(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup(final Validator validator) {
        for (final OrderTable orderTable : values) {
            orderTable.ungroup(validator);
        }
    }

    public List<OrderTable> getValues() {
        return values;
    }
}
