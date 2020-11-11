package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderTable;

import java.util.Objects;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableResponse() {
    }

    public OrderTableResponse(Long id) {
        this.id = id;
    }

    public OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        if (Objects.isNull(orderTable.getTableGroup())) {
            return new OrderTableResponse(orderTable.getId(), null,
                    orderTable.getNumberOfGuestsCount(), orderTable.isEmptyTable());
        }
        return new OrderTableResponse(orderTable.getId(), orderTable.getContainTableGroupId(),
                orderTable.getNumberOfGuestsCount(), orderTable.isEmptyTable());
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
