package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private void validate(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> extractIds() {
        return orderTables.stream()
            .map(o -> o.getId())
            .collect(Collectors.toList());
    }

    public void validateWithSaved(List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }
    }
}
