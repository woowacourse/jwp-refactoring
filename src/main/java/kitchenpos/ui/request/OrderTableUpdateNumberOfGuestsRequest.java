package kitchenpos.ui.request;

public class OrderTableUpdateNumberOfGuestsRequest {

    private Integer numberOfGuests;

    public OrderTableUpdateNumberOfGuestsRequest() {
    }

    public OrderTableUpdateNumberOfGuestsRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

}
