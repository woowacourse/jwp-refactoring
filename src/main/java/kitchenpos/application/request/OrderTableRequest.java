package kitchenpos.application.request;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableRequest() {
    }

    public OrderTableRequest(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    public boolean getEmpty() {
        return empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }
}
