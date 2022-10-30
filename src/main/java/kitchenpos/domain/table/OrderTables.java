package kitchenpos.domain.table;

import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;

import kitchenpos.domain.table.OrderTable;

public class OrderTables {

    private static final int MINIMUM_NUMBER_OF_TABLE = 2;

    private final List<OrderTable> value;

    public OrderTables(final List<OrderTable> value) {
        validateOrderTableSizeIsValid(value);
        this.value = value;
    }

    private void validateOrderTableSizeIsValid(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_NUMBER_OF_TABLE) {
            throw new IllegalArgumentException(String.format("테이블의 수가 2개 이상이어야 합니다. [%s]", orderTables.size()));
        }
    }

    public List<OrderTable> getValue() {
        return Collections.unmodifiableList(value);
    }
}
