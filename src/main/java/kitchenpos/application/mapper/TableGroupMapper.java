package kitchenpos.application.mapper;

import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;

import java.util.stream.Collectors;

public class TableGroupMapper {

    public static TableGroupResponse mapToTableGroupResponse(final TableGroup tableGroup, final OrderTables orderTables) {

        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
                orderTables.getOrderTables()
                        .stream()
                        .map(orderTable -> new OrderTableResponse(orderTable.getId(), tableGroup.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty()))
                        .collect(Collectors.toList()));
    }
}
