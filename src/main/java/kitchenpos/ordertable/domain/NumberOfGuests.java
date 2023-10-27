package kitchenpos.ordertable.domain;

import java.util.Objects;

public class NumberOfGuests {

    private final int value;

    public NumberOfGuests(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("손님 수는 0보다 작을 수 없습니다.");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NumberOfGuests that = (NumberOfGuests) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
