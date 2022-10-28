package kitchenpos.application.dto.request;

public class OrderTableCreateRequestDto {

    private int numberOfGuests;

    private boolean empty;

    public OrderTableCreateRequestDto(int numberOfGuests, boolean empty) {
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
