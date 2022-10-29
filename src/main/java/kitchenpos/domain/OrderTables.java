package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private final List<OrderTable> values;

    public OrderTables(final List<OrderTable> values) {
        this.values = values;
    }

    public static OrderTables fromCreate(final List<OrderTable> orderTables) {
        validateOrderTableSize(orderTables);
        validateEmptyOrderTable(orderTables);
        return new OrderTables(orderTables);
    }

    private static void validateOrderTableSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateEmptyOrderTable(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public void fillTableGroup(final TableGroup tableGroup) {
        for (OrderTable orderTable : this.values) {
            orderTable.fillOrderTableGroup(tableGroup.getId());
        }
    }

    public void upgroup() {
        for (final OrderTable orderTable : this.values) {
            orderTable.clear();
        }
    }

    public List<Long> getOrderIds() {
        return this.values.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getValues() {
        return values;
    }
}
