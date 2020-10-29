package kitchenpos.domain.order;

import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    private OrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void validateGroupTables() {
        if (isNonEmptyOrGroupTable()) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isNonEmptyOrGroupTable() {
        return orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmptyTable() || Objects.nonNull(orderTable.getTableGroup()));
    }

    public void isInvalidOrderTables(OrderTables orderTables) {
        if (this.orderTables.size() != orderTables.orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
