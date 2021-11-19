package kitchenpos.dto.table;

import kitchenpos.domain.table.TableGroup;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    @NotEmpty(message = "주문 테이블이 비어있습니다.")
    @Size(min = 2, message = "주문 테이블이 1테이블 이하입니다.")
    private List<OrderTableIdRequest> orderTables;

    private TableGroupRequest() {
    }

    private TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }


    public static TableGroupRequest from(List<OrderTableIdRequest> orderTableIdRequests) {
        return new TableGroupRequest(orderTableIdRequests);
    }

    public TableGroup toTableGroup() {
        return new TableGroup(null, LocalDateTime.now());
    }

    public List<Long> orderTableIds() {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());

    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}


