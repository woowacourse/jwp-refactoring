package kitchenpos.table.ui.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;

public class OrderTableCreateResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableCreateResponse() {
    }

    public OrderTableCreateResponse(final Long id, final Long tableGroupId, final int numberOfGuests,
                                    final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static List<OrderTableCreateResponse> from(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableCreateResponse::from)
                .collect(Collectors.toList());
    }

    private static OrderTableCreateResponse from(final OrderTable orderTable) {
        return new OrderTableCreateResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
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
