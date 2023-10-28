package kitchenpos.ordertable.application.dto.response;

import kitchenpos.ordertable.OrderTable;
import kitchenpos.tablegroup.TableGroup;

import java.util.Objects;

public class OrderTableResponse {

    private final Long id;
    private final int numberOfGuests;
    private final boolean empty;
    private final Long tableGroupId;

    public OrderTableResponse(final Long id, final int numberOfGuests, final boolean empty, final Long tableGroupId) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.tableGroupId = tableGroupId;
    }

    public static OrderTableResponse from(final OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getNumberOfGuests().getNumber(),
                orderTable.isEmpty(),
                ifTableGroupPresentOrNull(orderTable.getTableGroup())
        );
    }

    private static Long ifTableGroupPresentOrNull(final TableGroup tableGroup) {
        if (Objects.isNull(tableGroup)) {
            return null;
        }
        return tableGroup.getId();
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
