package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.IdRequest;
import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {
    public static TableGroup createTableGroup(Long id, LocalDateTime createdDate) {
        return new TableGroup(
            id,
            createdDate
        );
    }

    public static TableGroupCreateRequest createTableGroupRequest(List<OrderTable> orderTables) {
        return new TableGroupCreateRequest(
            orderTables.stream()
                .map(OrderTable::getId)
                .map(IdRequest::new)
                .collect(Collectors.toList())
        );
    }
}
