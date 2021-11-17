package kitchenpos.ui.dto;

import javax.validation.constraints.NotNull;

public class OrderTableRequest {

    @NotNull
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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
