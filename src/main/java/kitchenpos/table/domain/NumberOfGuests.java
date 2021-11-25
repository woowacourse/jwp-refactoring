package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import kitchenpos.table.exception.InvalidNumberOfGuestsException;

@Embeddable
public class NumberOfGuests {

    @NotNull
    @Column(name = "numberOfGuests")
    private int value;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int value) {
        this.value = value;
        validateNegative(this.value);
    }

    private void validateNegative(int value) {
        if (value < 0) {
            throw new InvalidNumberOfGuestsException(String.format("음수 %s는 손님 수가 될 수 없습니다.", value));
        }
    }

    public int getValue() {
        return value;
    }
}
