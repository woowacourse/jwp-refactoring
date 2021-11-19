package kitchenpos.domain.order;

import java.util.List;
import java.util.Objects;

public class Tables {

    private List<OrderTable> values;

    public Tables(List<OrderTable> values) {
        this.values = values;
    }

    public void validateSize(int size) {
        if (values.size() != size) {
            throw new IllegalArgumentException();
        }
    }

    public void validateCondition() {
        for (final OrderTable orderTable : values) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public void changeCondition(TableGroup tableGroup) {
        for (final OrderTable savedOrderTable : values) {
            savedOrderTable.changeTableGroup(tableGroup);
            savedOrderTable.changeEmpty(false);
        }
    }
}
