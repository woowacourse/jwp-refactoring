package kitchenpos.application.dto.convertor;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.table.OrderTable;

public class TableConvertor {

    private TableConvertor() {
    }

    public static OrderTable toOrderTable(final OrderTableRequest request) {
        return new OrderTable(request.getNumberOfGuests(), request.isEmpty());
    }

    public static OrderTableResponse toOrderTableResponse(final OrderTable orderTable) {
        return new OrderTableResponse(
            orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty()
        );
    }

    public static List<OrderTableResponse> toOrderTableResponses(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(orderTable -> new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty()))
            .collect(Collectors.toUnmodifiableList());
    }
}
