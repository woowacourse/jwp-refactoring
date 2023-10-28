package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    @Column(name = "numberOfGuests", nullable = false)
    private int value;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(final int value) {
        validateNumberOfGuests(value);

        this.value = value;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("인원 수는 음수일 수 없습니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
