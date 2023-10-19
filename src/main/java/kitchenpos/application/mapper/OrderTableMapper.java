package kitchenpos.application.mapper;

import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.OrderTable;

public class OrderTableMapper {

    private OrderTableMapper() {
    }

    public static OrderTable mapToOrderTable(final OrderTableCreateRequest orderTableCreateRequest) {
        return new OrderTable(orderTableCreateRequest.getNumberOfGuests(),
                orderTableCreateRequest.isEmpty());
    }

    public static OrderTable mapToOrderTable(final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest) {
        return new OrderTable(orderTableChangeEmptyRequest.getTableGroupId(),
                orderTableChangeEmptyRequest.getNumberOfGuests(),
                orderTableChangeEmptyRequest.isEmpty());
    }

    public static OrderTableResponse mapToResponse(final OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }
}
