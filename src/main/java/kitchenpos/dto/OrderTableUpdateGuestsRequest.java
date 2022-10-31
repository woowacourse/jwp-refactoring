package kitchenpos.dto;

public class OrderTableUpdateGuestsRequest {

    private Integer numberOfGuests;

    public OrderTableUpdateGuestsRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTableUpdateGuestsRequest() {
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
