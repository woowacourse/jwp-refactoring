package kitchenpos.ui.request;

public class OrderTableCreateRequest {

    private Integer numberOfGuests;
    private boolean empty;

    public OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(Integer numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean getEmpty() {
        return empty;
    }

}
