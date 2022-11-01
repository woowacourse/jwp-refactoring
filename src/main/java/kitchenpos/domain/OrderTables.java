package kitchenpos.domain;

import java.util.Collections;
import java.util.List;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private List<OrderTable> value;

    public OrderTables(List<OrderTable> orderTables) {
        checkSize(Collections.unmodifiableList(orderTables));
        this.value = orderTables;
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    private void checkSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void checkCanGroup() {
        for (final OrderTable savedOrderTable : value) {
            savedOrderTable.checkCanGroup();
        }
    }

    public List<OrderTable> getValue() {
        return value;
    }
}
