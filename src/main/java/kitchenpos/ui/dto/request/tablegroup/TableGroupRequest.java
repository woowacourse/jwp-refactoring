package kitchenpos.ui.dto.request.tablegroup;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.tablegroup.OrderTableGroupRequestDto;
import kitchenpos.application.dto.request.tablegroup.TableGroupRequestDto;

public class TableGroupRequest {

    List<OrderTableGroupRequest> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableGroupRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequestDto toDto() {
        List<OrderTableGroupRequestDto> dto = orderTables.stream()
            .map(OrderTableGroupRequest::toDto)
            .collect(Collectors.toList());
        return new TableGroupRequestDto(dto);
    }

    public List<OrderTableGroupRequest> getOrderTables() {
        return orderTables;
    }
}
