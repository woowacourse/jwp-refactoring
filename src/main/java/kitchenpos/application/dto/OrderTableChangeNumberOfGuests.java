package kitchenpos.application.dto;

import javax.validation.constraints.Min;

public class OrderTableChangeNumberOfGuests {
    @Min(0)
    private int numberOfGuests;

    private OrderTableChangeNumberOfGuests() {
    }

    public OrderTableChangeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
