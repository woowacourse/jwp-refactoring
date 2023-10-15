package kitchenpos.domain.ordertable;

import java.util.Objects;

public class Emptiness {
    public static final Emptiness EMPTY = new Emptiness(true);
    public static final Emptiness NOT_EMPTY = new Emptiness(false);

    private final boolean value;

    private Emptiness(final boolean isEmpty) {
        this.value = isEmpty;
    }

    public static Emptiness from(final boolean empty) {
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
        final Emptiness emptiness = (Emptiness) o;
        return value == emptiness.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
