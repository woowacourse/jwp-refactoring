package kitchenpos.dto.response;

import java.util.Objects;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableResponse() {
    }

    private OrderTableResponse(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(final OrderTable orderTable) {
        TableGroup tableGroup = orderTable.getTableGroup();
        if (Objects.isNull(tableGroup)) {
            return new OrderTableResponse(
                    orderTable.getId(),
                    null,
                    orderTable.getNumberOfGuests(),
                    orderTable.isEmpty());
        }
        return new OrderTableResponse(
                orderTable.getId(),
                tableGroup.getId(),
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
