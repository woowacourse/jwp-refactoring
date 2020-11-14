package kitchenpos.dto.request;

import java.beans.ConstructorProperties;

public class OrderTableChangeNumberOfGuestsRequest {
    private final int numberOfGuests;

    @ConstructorProperties({"numberOfGuests"})
    public OrderTableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
