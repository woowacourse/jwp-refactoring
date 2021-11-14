package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableResponse {

    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        if (orderTable.hasGroup()) {
            return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroup().getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
        }
        return new OrderTableResponse(orderTable.getId(), null, orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<OrderTableResponse> listOf(List<OrderTable> orderTables) {
        return orderTables.stream().map(OrderTableResponse::of).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
