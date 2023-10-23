package kitchenpos.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.dto.table.TableCreateRequest;

public class OrderTableMapper {

    private OrderTableMapper() {
    }

    public static OrderTable toOrderTable(
            final TableCreateRequest tableCreateRequest
    ) {
        return new OrderTable(
                null,
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
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public static List<OrderTableResponse> toOrderTableResponses(
            final List<OrderTable> orderTables
    ) {
        return orderTables.stream()
                .map(orderTable -> new OrderTableResponse(
                                orderTable.getId(),
                                orderTable.getTableGroupId(),
                                orderTable.getNumberOfGuests(),
                                orderTable.isEmpty()
                        )
                )
                .collect(Collectors.toList());
    }
}
