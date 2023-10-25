package kitchenpos.dto.ordertable;

import java.util.Objects;
import kitchenpos.domain.ordertable.OrderTable;

public class OrderTableResponse {

    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    public OrderTableResponse(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(final OrderTable orderTable) {
        if (Objects.isNull(orderTable.getTableGroup())) {
            return new OrderTableResponse(orderTable.getId(),
                null,
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
        }
        return new OrderTableResponse(orderTable.getId(),
            orderTable.getTableGroup().getId(),
            orderTable.getNumberOfGuests(),
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
