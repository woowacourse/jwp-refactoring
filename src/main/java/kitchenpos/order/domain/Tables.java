package kitchenpos.order.domain;

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
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public void changeCondition(Long tableGroupId) {
        for (final OrderTable savedOrderTable : values) {
            savedOrderTable.changeTableGroupId(tableGroupId);
            savedOrderTable.changeEmpty(false);
        }
    }
}
