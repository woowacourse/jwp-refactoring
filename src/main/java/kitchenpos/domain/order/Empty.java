package kitchenpos.domain.order;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Empty {
    private boolean empty;

    protected Empty() {
    }

    private Empty(final boolean empty) {
        this.empty = empty;
    }

    public static Empty of(final boolean empty) {
        return new Empty(empty);
    }

    public Empty compare(final boolean empty) {
        if (this.empty == empty) {
            return this;
        }
        return new Empty(empty);
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Empty empty = (Empty) o;
        return this.empty == empty.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empty);
    }
}
