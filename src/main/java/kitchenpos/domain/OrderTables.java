package kitchenpos.domain;

import java.util.List;

public class OrderTables {
    private List<OrderTable> values;

    public OrderTables(final List<OrderTable> values) {
        this.values = values;
    }

    public List<OrderTable> getValues() {
        return values;
    }
}
