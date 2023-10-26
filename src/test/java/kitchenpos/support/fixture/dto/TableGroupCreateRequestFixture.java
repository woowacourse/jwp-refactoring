package kitchenpos.support.fixture.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ordertable.OrderTableIdDto;
import kitchenpos.application.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.domain.OrderTable;

public abstract class TableGroupCreateRequestFixture {

    public static TableGroupCreateRequest tableGroupCreateRequest(final List<OrderTable> orderTables) {
        final List<OrderTableIdDto> orderTableIdDtos = orderTables.stream()
                .map(orderTable -> new OrderTableIdDto(orderTable.getId()))
                .collect(Collectors.toList());
        return new TableGroupCreateRequest(orderTableIdDtos);
    }
}
