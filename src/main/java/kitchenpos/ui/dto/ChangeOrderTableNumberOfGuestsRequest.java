package kitchenpos.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.application.dto.ordertable.ChangeOrderTableNumberOfGuestsCommand;

public class ChangeOrderTableNumberOfGuestsRequest {

    @JsonProperty("numberOfGuests")
    private int numberOfGuests;

    public ChangeOrderTableNumberOfGuestsRequest() {
    }

    public ChangeOrderTableNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public ChangeOrderTableNumberOfGuestsCommand toCommand(Long orderTableId) {
        return new ChangeOrderTableNumberOfGuestsCommand(orderTableId, numberOfGuests);
    }
}
