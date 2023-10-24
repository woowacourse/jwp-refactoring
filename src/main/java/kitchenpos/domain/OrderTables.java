package kitchenpos.domain;

import java.util.List;

public class OrderTables {

    private final List<OrderTable> tables;

    public OrderTables(final List<OrderTable> tables) {
        this.tables = tables;
    }

    public void checkTableSizeIsEqual(final int tableSize) {
        if (tables.size() != tableSize) {
            throw new IllegalArgumentException("테이블의 수가 일치하지 않습니다.");
        }
    }

    public void checkEmptyAndTableGroups() {
        tables.forEach(OrderTable::checkEmptyAndTableGroups);
    }

    public List<OrderTable> getTables() {
        return tables;
    }
}
