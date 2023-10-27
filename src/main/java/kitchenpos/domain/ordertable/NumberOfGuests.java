package kitchenpos.domain.ordertable;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.springframework.data.relational.core.mapping.Column;

public class NumberOfGuests {
    @Column("NUMBER_OF_GUESTS")
    @JsonProperty("numberOfGuests")
    private final int value;

    public NumberOfGuests(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }

        this.value = value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NumberOfGuests that = (NumberOfGuests) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
