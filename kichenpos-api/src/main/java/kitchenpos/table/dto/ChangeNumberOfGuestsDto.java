package kitchenpos.table.dto;

public class ChangeNumberOfGuestsDto {

    private final int numberOfGuests;

    public ChangeNumberOfGuestsDto(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
