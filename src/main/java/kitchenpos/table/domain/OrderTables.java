package kitchenpos.table.domain;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OrderTables {
    private final List<OrderTable> values;

    public OrderTables(List<OrderTable> values) {
        validateExists(values);
        this.values = values;
    }

    private void validateExists(List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || savedOrderTable.getTableGroupId() != null) {
                throw new IllegalArgumentException("[ERROR] 주문 테이블이 이미 존재합니다.");
            }
        }
    }

    public void validateOrderTableSize(int orderTableSize) {
        if (values.size() != orderTableSize) {
            throw new IllegalArgumentException("[ERROR] 저장된 주문 테이블과 요청의 개수가 맞지 않습니다.");
        }
    }

    public List<OrderTable> mapToOrderTables(Consumer<OrderTable> consumer) {
        return values.stream()
                .peek(it -> consumer.accept(new OrderTable(it.getTableGroupId(), it.getNumberOfGuests(), false)))
                .collect(Collectors.toList());
    }
}
