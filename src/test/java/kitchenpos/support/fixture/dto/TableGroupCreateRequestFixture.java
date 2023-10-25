package kitchenpos.support.fixture.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.application.dto.ordertable.OrderTableIdDto;
import kitchenpos.application.dto.tablegroup.TableGroupCreateRequest;

public class TableGroupCreateRequestFixture {

    public static TableGroupCreateRequest tableGroupCreateRequest(final List<OrderTable> orderTables) {
        final List<OrderTableIdDto> orderTableIdDtos = orderTables.stream()
                .map(orderTable -> new OrderTableIdDto(orderTable.getId()))
                .collect(Collectors.toList());
        return new TableGroupCreateRequest(orderTableIdDtos);
    }
}
