package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupFixture {

    private static final Long ID = 1L;
    private static final LocalDateTime CREATED_DATE = LocalDateTime.now();

    public static TableGroup createTableGroup() {
        return new TableGroup(CREATED_DATE);
    }

    public static TableGroupRequest createTableGroupRequest(OrderTable... orderTables) {
        List<Long> ids = Arrays.stream(orderTables)
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return new TableGroupRequest(CREATED_DATE, ids);
    }

    public static TableGroupRequest createTableGroupRequest(Long... orderTableIds) {
        return new TableGroupRequest(CREATED_DATE, Arrays.asList(orderTableIds));
    }

    public static TableGroupResponse createTableGroupResponse(Long id, TableGroupRequest request) {
        List<OrderTable> orderTables = new ArrayList<>();
        for(long orderTableId : request.getOrderTableIds()){
            OrderTable orderTable = new OrderTable(1, true);
            orderTable.setId(orderTableId);
            orderTables.add(orderTable);
        }
        return new TableGroupResponse(id, request.getCreatedDate(), orderTables);
    }
}
