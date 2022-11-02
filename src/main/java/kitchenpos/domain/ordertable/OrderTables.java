package kitchenpos.domain.ordertable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private static final int MIN_SIZE = 2;

    private List<OrderTable> values;

    public OrderTables(final List<OrderTable> values) {
        checkSize(values);
        this.values = new ArrayList<>(values);
    }

    public void joinGroup(final TableGroup tableGroup) {
        for (OrderTable orderTable : values) {
            orderTable.joinGroup(tableGroup);
        }
    }

    public void ungroup() {
        for (OrderTable orderTable : values) {
            orderTable.ungroup();
        }
    }

    private void checkSize(final List<OrderTable> values) {
        if (CollectionUtils.isEmpty(values) || values.size() < MIN_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> getIds() {
        final List<Long> ids = values.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(ids);
    }

    public List<OrderTable> getValues() {
        return Collections.unmodifiableList(values);
    }

    protected OrderTables() {
    }
}
