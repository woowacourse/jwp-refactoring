package kitchenpos.ui.dto;

import java.util.List;
import java.util.Objects;
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;
import org.springframework.util.CollectionUtils;

public class TableGroupAssembler {

    private TableGroupAssembler() {
    }

    public static TableGroup assemble(TableGroupRequest tableGroupRequest,
        List<Table> foundTables) {
        List<TableOfTableGroupRequest> tables = tableGroupRequest.getOrderTables();
        validate(tables, foundTables);
        return TableGroup.entityOf(foundTables);
    }

    private static void validate(List<TableOfTableGroupRequest> tables, List<Table> foundTables) {
        if (CollectionUtils.isEmpty(tables) || tables.size() < 2) {
            throw new IllegalArgumentException("테이블은 2개 이상이어야 합니다.");
        }

        if (tables.size() != foundTables.size()) {
            throw new IllegalArgumentException("올바르지 않은 테이블 ID 입니다.");
        }

        boolean existsTable = foundTables.stream()
            .anyMatch(foundTable -> Objects.nonNull(foundTable.getTableGroup()));
        if (existsTable) {
            throw new IllegalArgumentException("이미 Group이 지정되었습니다.");
        }
    }
}
