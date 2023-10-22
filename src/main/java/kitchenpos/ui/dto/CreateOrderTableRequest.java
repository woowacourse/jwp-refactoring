package kitchenpos.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.application.dto.ordertable.CreateOrderTableCommand;

public class CreateOrderTableRequest {

    @JsonProperty("numberOfGuests")
    private final int numberOfGuests;
    @JsonProperty("empty")
    private final boolean empty;

    public CreateOrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public CreateOrderTableCommand toCommand() {
        return new CreateOrderTableCommand(numberOfGuests, empty);
    }
}
