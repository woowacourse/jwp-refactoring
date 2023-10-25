package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderTableGuestNumberException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class GuestNumber {

    private Integer numberOfGuests;

    protected GuestNumber() {
    }

    public GuestNumber(Integer numberOfGuests) {
        validateGuestNumber(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateGuestNumber(Integer numberOfGuests) {
        if (isNull(numberOfGuests) || isNegative(numberOfGuests)) {
            throw new OrderTableGuestNumberException("게스트의 수는 0명 이상이어야 합니다.");
        }
    }

    private boolean isNull(Integer numberOfGuests) {
        return Objects.isNull(numberOfGuests);
    }

    private boolean isNegative(Integer numberOfGuests) {
        return numberOfGuests < 0;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuestNumber that = (GuestNumber) o;
        return Objects.equals(getNumberOfGuests(), that.getNumberOfGuests());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumberOfGuests());
    }
}
