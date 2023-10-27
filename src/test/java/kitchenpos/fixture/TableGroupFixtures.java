package kitchenpos.fixture;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.request.TableGroupUnitDto;

public class TableGroupFixtures {

    public static TableGroupCreateRequest getTableGroupCreateRequest(List<OrderTable> orderTables) {
        return new TableGroupCreateRequest(orderTables.stream()
                .map(orderTable -> new TableGroupUnitDto(orderTable.getId()))
                .collect(Collectors.toList()));
    }
}
