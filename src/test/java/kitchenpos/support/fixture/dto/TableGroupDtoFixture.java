package kitchenpos.support.fixture.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.dto.OrderTableIdDto;
import kitchenpos.table.application.dto.TableGroupResponse;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.application.dto.TableGroupSaveRequest;

public class TableGroupDtoFixture {

    public static TableGroupSaveRequest 단체_지정_생성_요청(OrderTables orderTables) {
        List<OrderTableIdDto> orderTableIds = orderTables.getOrderTables().stream()
            .map(it -> new OrderTableIdDto(it.getId()))
            .collect(Collectors.toList());
        return new TableGroupSaveRequest(orderTableIds);
    }

    public static TableGroupResponse 단체_지정_생성_응답(TableGroup tableGroup, OrderTables orderTables) {
        return TableGroupResponse.toResponse(tableGroup, orderTables);
    }
}
