package kitchenpos.ui.dto.request;

public class OrderTableChangeNumberOfGuestsRequest {

    private Integer numberOfGuests;

    private OrderTableChangeNumberOfGuestsRequest() {
    }

    public OrderTableChangeNumberOfGuestsRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
