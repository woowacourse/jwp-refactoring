package kitchenpos.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class GuestsNumber {

    @Column(name = "number_of_guests")
    private int value;

    public GuestsNumber() {
    }

    public GuestsNumber(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("손님 수는 음수일 수 없습니다.");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GuestsNumber that = (GuestsNumber) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
