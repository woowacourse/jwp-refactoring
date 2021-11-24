package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderTables {
    private List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = new ArrayList<>(orderTables);
    }

    private void validate(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTable> getOrderTables() {
        return new ArrayList<>(orderTables);
    }

    public void checkValidity(OrderTables other) {
        if (orderTables.size() != other.orderTables.size()) {
            throw new IllegalArgumentException();
        }
        orderTables.forEach(OrderTable::check);
    }

    public void update(Long tableGroupId, boolean empty) {
        orderTables.forEach(it -> it.update(tableGroupId, empty));
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
