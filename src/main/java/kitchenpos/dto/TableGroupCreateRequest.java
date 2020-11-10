package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {
    @NotEmpty(message = "주문 테이블 목록은 반드시 존재해야 합니다!")
    private List<OrderTableCreateRequest> orderTableCreateRequests;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableCreateRequest> orderTableCreateRequests) {
        this.orderTableCreateRequests = orderTableCreateRequests;
    }

    public TableGroup toTableGroup(LocalDateTime localDateTime) {
        List<OrderTable> orderTables = orderTableCreateRequests.stream()
                .map(OrderTableCreateRequest::toOrderTable)
                .collect(Collectors.toList());

        return new TableGroup(localDateTime, orderTables);
    }

    public List<OrderTableCreateRequest> getOrderTableCreateRequests() {
        return orderTableCreateRequests;
    }
}
