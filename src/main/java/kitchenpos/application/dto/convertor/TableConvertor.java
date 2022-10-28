package kitchenpos.application.dto.convertor;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.OrderTable;

public class TableConvertor {

    private TableConvertor() {
    }

    public static OrderTable convertToOrderTable(final OrderTableRequest request) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(request.isEmpty());
        orderTable.setNumberOfGuests(request.getNumberOfGuests());
        return orderTable;
    }

    public static OrderTableResponse convertToOrderTableResponse(final OrderTable orderTable) {
        return new OrderTableResponse(
            orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty()
        );
    }

    public static List<OrderTableResponse> convertToOrderTableResponses(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(orderTable -> new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty()))
            .collect(Collectors.toUnmodifiableList());
    }
}
