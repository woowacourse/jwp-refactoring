package kitchenpos.domain;

import com.sun.istack.NotNull;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    @NotNull
    @Column(name = "numberOfGuests")
    private int value;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int value) {
        validateNegative(value);
        this.value = value;
    }

    public static NumberOfGuests create(int value){
        return new NumberOfGuests(value);
    }

    private void validateNegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException(("손님은 0명 이상이어야 합니다."));
        }
    }

    public int getValue() {
        return value;
    }
}
