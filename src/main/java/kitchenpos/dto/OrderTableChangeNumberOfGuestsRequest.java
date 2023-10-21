package kitchenpos.dto;

public class OrderTableChangeNumberOfGuestsRequest {

    private Integer numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest() {
    }

    public OrderTableChangeNumberOfGuestsRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
