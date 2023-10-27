package kitchenpos.fixture;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.request.TableGroupCreateRequest;
import kitchenpos.request.TableGroupUnitDto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupFixtures {

    public static TableGroupCreateRequest getTableGroupCreateRequest(List<OrderTable> orderTables) {
        return new TableGroupCreateRequest(orderTables.stream()
                .map(orderTable -> new TableGroupUnitDto(orderTable.getId()))
                .collect(Collectors.toList()));
    }
}
