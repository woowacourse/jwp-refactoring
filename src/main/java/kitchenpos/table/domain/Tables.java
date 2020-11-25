package kitchenpos.table.domain;

import java.util.List;

public class Tables {
    private final List<Table> tables;

    public Tables(List<Table> tables) {
        this.tables = tables;
    }

    public void unGroup() {
        for (final Table table : tables) {
            table.changeTableGroup(null);
        }
    }

    public List<Table> getTables() {
        return tables;
    }
}
