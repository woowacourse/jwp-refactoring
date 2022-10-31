package kitchenpos.dto;

public class OrderTableCreateRequest {

    private Integer numberOfGuests;
    private boolean empty;

    public OrderTableCreateRequest(Integer numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableCreateRequest() {
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean getEmpty() {
        return empty;
    }
}
