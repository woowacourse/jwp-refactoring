package core.vo;

import java.util.Objects;

public class NumberOfGuest {

    private final int value;

    public NumberOfGuest(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("주문 테이블의 손님 수가 0보다 커야합니다");
        }
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        NumberOfGuest that = (NumberOfGuest) object;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
