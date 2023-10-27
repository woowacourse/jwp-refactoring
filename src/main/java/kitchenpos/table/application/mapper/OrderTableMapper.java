package kitchenpos.table.application.mapper;

import kitchenpos.table.application.dto.request.OrderTableCreateRequest;
import kitchenpos.table.application.dto.response.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;

public class OrderTableMapper {

    private OrderTableMapper() {
    }

    public static OrderTable mapToOrderTable(final OrderTableCreateRequest orderTableCreateRequest) {
        return new OrderTable(orderTableCreateRequest.getNumberOfGuests(),
                orderTableCreateRequest.isEmpty());
    }

    public static OrderTableResponse mapToResponse(final OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }
}
