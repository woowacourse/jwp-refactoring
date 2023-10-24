package kitchenpos.table.domain;

public class NumberOfGuests {

    public static final NumberOfGuests DEFAULT_NUMBER_OF_GUESTS = new NumberOfGuests(0);
    private static final int MIN_NUMBER_OF_GUESTS = 0;
    private final int value;


    public NumberOfGuests(final int value) {
        validateNumberOfGuests(value);
        this.value = value;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException();
        }
    }

    public int getValue() {
        return value;
    }
}
