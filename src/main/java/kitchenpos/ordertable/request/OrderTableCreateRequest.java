package kitchenpos.ordertable.request;

public class OrderTableCreateRequest {

    private final Integer numberOfGuests;
    private final Boolean empty;

    public OrderTableCreateRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
