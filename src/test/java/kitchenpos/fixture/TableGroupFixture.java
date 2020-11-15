package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.IdRequest;
import kitchenpos.ui.dto.TableGroupCreateRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupFixture {
    public static TableGroupCreateRequest createTableGroupRequest(List<Long> orderTableIds) {
        List<IdRequest> orderTableIdRequests = orderTableIds.stream()
                .map(IdRequest::new)
                .collect(Collectors.toList());
        return new TableGroupCreateRequest(orderTableIdRequests);
    }

    public static TableGroup createTableGroup(Long id, LocalDateTime createdDateTime, List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableIds.stream()
                .map(it -> new OrderTable(it, null, 1, false))
                .collect(Collectors.toList());
        return new TableGroup(id, createdDateTime, orderTables);
    }
}
