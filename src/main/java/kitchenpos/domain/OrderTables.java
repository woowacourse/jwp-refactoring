package kitchenpos.domain;

import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_TABLE_EXCEPTION;

import java.util.List;
import java.util.Objects;
import kitchenpos.application.exception.CustomIllegalArgumentException;

public class OrderTables {
    private List<OrderTable> values;

    public OrderTables(final List<OrderTable> values) {
        validate(values);
        this.values = values;
    }

    private void validate(final List<OrderTable> values) {
        for (final OrderTable value : values) {
            if (!value.isEmpty() || Objects.nonNull(value.getTableGroupId())) {
                throw new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION);
            }
        }
    }

    public List<OrderTable> getValues() {
        return values;
    }
}
