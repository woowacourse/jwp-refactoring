package kitchenpos.ui.dto;

public class OrderTableCreateRequest {

    private boolean isEmpty;
    private int numberOfGuests;

    private OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(boolean isEmpty, int numberOfGuests) {
        this.isEmpty = isEmpty;
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
