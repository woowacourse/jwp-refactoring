package kitchenpos.application.mapper;

import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
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
}
