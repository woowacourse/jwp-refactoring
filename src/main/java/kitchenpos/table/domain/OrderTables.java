package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> tables = new ArrayList<>();

    public OrderTables() {
    }

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

    public void addTable(final OrderTable table) {
        tables.add(table);
    }

    public List<OrderTable> getTables() {
        return tables;
    }
}
