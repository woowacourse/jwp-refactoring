package kitchenpos.application.dto.request;

public class OrderTableCreateReqeust {

    private int numberOfGuests;
    private boolean empty;

    private OrderTableCreateReqeust() {
    }

    public OrderTableCreateReqeust(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
