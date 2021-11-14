package kitchenpos.order.fixture;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupFixture {

    public static TableGroupRequest createTableGroupRequest(OrderTable... orderTables) {
        List<Long> ids = Arrays.stream(orderTables)
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return new TableGroupRequest(ids);
    }

    public static TableGroupRequest createTableGroupRequest(Long... orderTableIds) {
        return new TableGroupRequest(Arrays.asList(orderTableIds));
    }

    public static TableGroupResponse createTableGroupResponse(Long id, TableGroupRequest request) {
        List<OrderTable> orderTables = new ArrayList<>();
        for(long orderTableId : request.getOrderTableIds()){
            OrderTable orderTable = new OrderTable(1, true);
            orderTable.setId(orderTableId);
            orderTables.add(orderTable);
        }
        return new TableGroupResponse(id, LocalDateTime.now(), orderTables);
    }
}
