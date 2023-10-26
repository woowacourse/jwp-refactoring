package kitchenpos.table.application.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableCreateRequest;

public class OrderTableMapper {

    private OrderTableMapper() {
    }

    public static OrderTable toOrderTable(
            final TableCreateRequest tableCreateRequest
    ) {
        return new OrderTable(
                null,
                tableCreateRequest.getNumberOfGuests(),
                tableCreateRequest.isEmpty()
        );
    }

    public static OrderTableResponse toOrderTableResponse(
            final OrderTable orderTable
    ) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroupId().orElse(null),
                orderTable.getNumberOfGuests().getValue(),
                orderTable.isEmpty()
        );
    }

    public static List<OrderTableResponse> toOrderTableResponses(
            final List<OrderTable> orderTables
    ) {
        return orderTables.stream()
                .map(OrderTableMapper::toOrderTableResponse)
                .collect(Collectors.toList());
    }
}
