package kitchenpos.fixture.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.TableGroupRequest;

public class TableGroupDtoFixture {

    public static TableGroupRequest createTableGroupRequest(final OrderTable... orderTables) {
        final List<Long> orderTableIds = Arrays.stream(orderTables)
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        return new TableGroupRequest(orderTableIds);
    }
}
