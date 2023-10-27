package kitchenpos.table.application.dto.response;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableQueryResponse {

    private Long id;
    private Long tableGroupId;


    private int numberOfGuests;
    private boolean empty;

    public OrderTableQueryResponse(final Long id, final Long tableGroupId, final int numberOfGuests,
                                   final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableQueryResponse() {
    }

    public static List<OrderTableQueryResponse> from(final OrderTables orderTables) {
        return orderTables.getOrderTables()
                .stream()
                .map(OrderTableQueryResponse::from)
                .collect(Collectors.toList());
    }

    public static OrderTableQueryResponse from(final OrderTable orderTable) {
        return new OrderTableQueryResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests().getValue(),
                orderTable.isEmpty());
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
