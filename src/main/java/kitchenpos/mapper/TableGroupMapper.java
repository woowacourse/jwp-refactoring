package kitchenpos.mapper;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.table.TableGroupResponse;

public class TableGroupMapper {

    private TableGroupMapper() {
    }

    public static TableGroup toTableGroup(
            final LocalDateTime createdDate,
            final List<OrderTable> orderTables
    ) {
        return new TableGroup(
                null,
                createdDate,
                orderTables
        );
    }

    public static TableGroupResponse toTableGroupResponse(
            final TableGroup tableGroup
    ) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                OrderTableMapper.toOrderTableResponses(tableGroup.getOrderTables())
        );
    }
}
