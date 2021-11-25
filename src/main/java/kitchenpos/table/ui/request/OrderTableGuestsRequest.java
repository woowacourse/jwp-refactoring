package kitchenpos.table.ui.request;

import javax.validation.constraints.Min;

public class OrderTableGuestsRequest {

    @Min(value = 0, message = "손님 수는 음수일 수 없습니다.")
    private int numberOfGuests;

    protected OrderTableGuestsRequest() {
    }

    public OrderTableGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
