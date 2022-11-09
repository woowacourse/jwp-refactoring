package kitchenpos.table.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private final List<OrderTable> value;

    public OrderTables(final List<OrderTable> value) {
        validateSize(value);
        this.value = value;
    }

    private void validateSize(final List<OrderTable> value) {
        if (CollectionUtils.isEmpty(value) || value.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> getIds() {
        return value.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getValue() {
        return value;
    }
}
