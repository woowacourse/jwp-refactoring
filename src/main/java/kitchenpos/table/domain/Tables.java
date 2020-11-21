package kitchenpos.table.domain;

import java.util.List;

import kitchenpos.order.exception.IllegalOrderStatusException;

public class Tables {

    private final List<Table> tables;

    public Tables(List<Table> tables) {
        this.tables = tables;
    }

    public void unGroup() {
        checkOrderStatus();
        for (final Table table : tables) {
            table.changeTableGroup(null);
        }
    }

    private void checkOrderStatus() {
        if (tables.stream()
            .anyMatch(Table::isNotCompletion)) {
            throw new IllegalOrderStatusException("OrderStatus의 상태가 Ungroup할 수 없습니다.");
        }
    }
}
