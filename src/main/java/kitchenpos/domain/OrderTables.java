package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OrderTables {
    private final List<OrderTable> values;

    public OrderTables(List<OrderTable> values) {
        validateEmpty(values);
        this.values = values;
    }

    private void validateEmpty(List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException("[ERROR] 주문 테이블이 비어있습니다.");
            }
        }
    }

    public void validateOrderTableSize(int orderTableSize) {
        if (values.size() != orderTableSize) {
            throw new IllegalArgumentException("[ERROR] 저장된 주문 테이블과 요청의 개수가 맞지 않습니다.");
        }
    }

    public List<OrderTable> mapToTableGroup(Consumer<OrderTable> consumer) {
        return values.stream()
                .peek(it -> consumer.accept(new OrderTable(it.getTableGroupId(), it.getNumberOfGuests(), false)))
                .collect(Collectors.toList());
    }
}
