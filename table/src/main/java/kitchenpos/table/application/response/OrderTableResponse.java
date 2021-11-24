package kitchenpos.table.application.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private long numberOfGuests;
    private boolean empty;

    public static OrderTableResponse from(OrderTable orderTable) {
        final OrderTableResponse orderTableResponse = new OrderTableResponse();
        orderTableResponse.id = orderTable.getId();
        orderTableResponse.tableGroupId = orderTable.getTableGroupId();
        orderTableResponse.numberOfGuests = orderTable.getNumberOfGuests();
        orderTableResponse.empty = orderTable.isEmpty();
        return orderTableResponse;
    }

    public static List<OrderTableResponse> fromList(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public long getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
