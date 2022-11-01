package kitchenpos.table.application.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest(final OrderTable orderTable) {
        this(orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public OrderTableRequest(final int numberOfGuests, final boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTableRequest(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    @JsonCreator
    public OrderTableRequest(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableRequest from(final OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
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
