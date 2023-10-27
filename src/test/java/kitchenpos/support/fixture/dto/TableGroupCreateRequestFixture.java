package kitchenpos.support.fixture.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.application.dto.OrderTableIdDto;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequest;
import kitchenpos.ordertable.domain.OrderTable;

public abstract class TableGroupCreateRequestFixture {

    public static TableGroupCreateRequest tableGroupCreateRequest(final List<OrderTable> orderTables) {
        final List<OrderTableIdDto> orderTableIdDtos = orderTables.stream()
                .map(orderTable -> new OrderTableIdDto(orderTable.getId()))
                .collect(Collectors.toList());
        return new TableGroupCreateRequest(orderTableIdDtos);
    }
}
