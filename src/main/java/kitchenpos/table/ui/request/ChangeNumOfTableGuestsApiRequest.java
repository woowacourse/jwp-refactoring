package kitchenpos.table.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import kitchenpos.table.application.request.ChangeNumOfTableGuestsRequest;

public class ChangeNumOfTableGuestsApiRequest {

    private final int numberOfGuests;

    @JsonCreator
    public ChangeNumOfTableGuestsApiRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public ChangeNumOfTableGuestsRequest toServiceRequest(Long orderTableId) {
        return new ChangeNumOfTableGuestsRequest(orderTableId, numberOfGuests);
    }
}
