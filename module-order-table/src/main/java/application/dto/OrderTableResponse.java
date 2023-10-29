package application.dto;

import domain.OrderTable;
import domain.TableGroup;
import java.util.Objects;
import support.AggregateReference;

public class OrderTableResponse {

    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    private OrderTableResponse(
            final Long id,
            final Long tableGroupId,
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
                getTableGroupId(orderTable.getTableGroup()),
                orderTable.getGuestStatus().getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    private static Long getTableGroupId(final AggregateReference<TableGroup> tableGroupId) {
        if (Objects.nonNull(tableGroupId)) {
            return tableGroupId.getId();
        }
        return null;
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
