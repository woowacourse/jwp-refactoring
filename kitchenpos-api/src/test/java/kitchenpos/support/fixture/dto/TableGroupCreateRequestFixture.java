package kitchenpos.support.fixture.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.ordertable.dto.OrderTableIdDto;
import kitchenpos.application.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.ordertable.OrderTable;

public abstract class TableGroupCreateRequestFixture {

    public static TableGroupCreateRequest tableGroupCreateRequest(final List<OrderTable> orderTables) {
        final List<OrderTableIdDto> orderTableIdDtos = orderTables.stream()
                .map(orderTable -> new OrderTableIdDto(orderTable.getId()))
                .collect(Collectors.toList());
        return new TableGroupCreateRequest(orderTableIdDtos);
    }
}
