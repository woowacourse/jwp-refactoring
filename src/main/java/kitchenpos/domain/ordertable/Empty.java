package kitchenpos.domain.ordertable;

import java.util.Objects;

public class Empty {
    public static final Empty EMPTY = new Empty(true);
    public static final Empty NOT_EMPTY = new Empty(false);

    private final boolean value;

    private Empty(final boolean isEmpty) {
        this.value = isEmpty;
    }

    public static Empty from(final boolean empty) {
        if (empty) {
            return EMPTY;
        }
        return NOT_EMPTY;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Empty empty = (Empty) o;
        return value == empty.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
