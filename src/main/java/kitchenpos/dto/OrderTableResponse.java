package kitchenpos.dto;

import io.micrometer.core.lang.Nullable;
import kitchenpos.table.domain.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableResponse {

    private long id;
    @Nullable
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse(final long id,
                              @Nullable final Long tableGroupId,
                              final int numberOfGuests,
                              final boolean empty
    ) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(final OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                getTableGroupId(orderTable),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    @Nullable
    private static Long getTableGroupId(final OrderTable orderTable) {
        if (orderTable.getTableGroupId() == null) {
            return null;
        }
        return orderTable.getTableGroupId();
    }

    public static List<OrderTableResponse> from(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    @Nullable
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
