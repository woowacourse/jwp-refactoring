package kitchenpos.application.dto.request;

import kitchenpos.domain.OrderTable;

public class OrderTableCreateRequest {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(final Long id,
                                   final Long tableGroupId,
                                   final int numberOfGuests,
                                   final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(null, null, this.numberOfGuests, this.empty);
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
