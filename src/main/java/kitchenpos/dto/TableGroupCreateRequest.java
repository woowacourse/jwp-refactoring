package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupCreateRequest {

    private List<Long> orderTableIds;

    private TableGroupCreateRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public static TableGroupCreateRequest of(TableGroup tableGroup) {
        List<OrderTable> orderTables = tableGroup.getOrderTables();
        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        return new TableGroupCreateRequest(orderTableIds);
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public TableGroup toEntity(LocalDateTime createDate, List<OrderTable> orderTables) {
        return new TableGroup(createDate, orderTables);
    }
}