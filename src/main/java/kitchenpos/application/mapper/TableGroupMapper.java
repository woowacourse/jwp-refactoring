package kitchenpos.application.mapper;

import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupMapper {

    private TableGroupMapper() {
    }

    public static TableGroup mapToTableGroup(final TableGroupCreateRequest tableGroupCreateRequest) {
        final List<OrderTable> orderTables = tableGroupCreateRequest.getOrderTables()
                .stream()
                .map(it -> new OrderTable(it.getNumberOfGuests(), it.isEmpty()))
                .collect(Collectors.toList());
        return new TableGroup(orderTables);
    }
}
