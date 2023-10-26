package kitchenpos.application.dto.request;

public class OrderTableCreateRequest {

    final int numberOfGuests;
    final boolean emtpy;

    public OrderTableCreateRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.emtpy = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean getEmtpy() {
        return emtpy;
    }
}
