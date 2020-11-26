package kitchenpos.ui.dto;

import java.util.List;
import kitchenpos.domain.table.Table;
import kitchenpos.domain.tablegroup.TableGroup;

public class TableGroupAssembler {

    private TableGroupAssembler() {
    }

    public static TableGroup assemble(List<Table> foundTables) {
        return TableGroup.entityOf(foundTables);
    }
}
