package kitchenpos.application.dto;

public class OrderTableCreateDto {

    private final int numberOfGuests;

    public OrderTableCreateDto(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
