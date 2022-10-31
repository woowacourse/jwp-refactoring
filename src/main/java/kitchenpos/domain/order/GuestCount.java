package kitchenpos.domain.order;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class GuestCount {

    @Column(name = "number_of_guests", nullable = false)
    private int value;

    public GuestCount(final int numberOfGuests) {
        validateNotNegative(numberOfGuests);
        this.value = numberOfGuests;
    }

    protected GuestCount() {
    }

    private void validateNotNegative(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 음수가 아니어야 합니다.");
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GuestCount that = (GuestCount) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
