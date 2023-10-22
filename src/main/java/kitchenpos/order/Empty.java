package kitchenpos.order;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Empty {
    public static final Empty EMPTY = new Empty(true);
    public static final Empty NOT_EMPTY = new Empty(false);

    private boolean empty;

    protected Empty() {
    }

    private Empty(final boolean empty) {
        this.empty = empty;
    }

    public static Empty from(final boolean empty) {
        if (empty) {
            return EMPTY;
        }
        return NOT_EMPTY;
    }

    public boolean getEmpty() {
        return empty;
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
        return this.empty == empty.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empty);
    }
}
