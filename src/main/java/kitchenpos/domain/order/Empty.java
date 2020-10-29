package kitchenpos.domain.order;

import javax.persistence.Embeddable;

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
}
