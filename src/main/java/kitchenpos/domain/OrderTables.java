package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        validateSize(orderTables);
        this.orderTables = orderTables;
    }

    private void validateSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void validateIsSameSize(List<OrderTable> orderTables) {
        if (this.orderTables.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }
}
