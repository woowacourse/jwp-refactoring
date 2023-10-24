package kitchenpos.dto;

public class TableChangeNumberOfGuestsDto {

    private final int numberOfGuests;

    public TableChangeNumberOfGuestsDto(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
