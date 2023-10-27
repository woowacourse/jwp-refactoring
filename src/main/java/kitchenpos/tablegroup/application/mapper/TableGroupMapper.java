package kitchenpos.tablegroup.application.mapper;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.application.mapper.OrderTableMapper;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupResponse;

public class TableGroupMapper {

    private TableGroupMapper() {
    }

    public static TableGroup toTableGroup(
            final LocalDateTime createdDate,
            final List<OrderTable> orderTables
    ) {
        return new TableGroup(
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
