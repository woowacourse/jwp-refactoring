package kitchenpos.domain.table;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;

@Embeddable
public class Empty {

    @Column(name = "empty")
    private boolean value;

    protected Empty() {
    }

    public Empty(final boolean value) {
        this.value = value;
    }

    public Empty changeTo(final boolean value) {
        validateNotSame(value);
        return new Empty(value);
    }

    private void validateNotSame(final boolean value) {
        if (this.value == value) {
            throw new DomainLogicException(CustomError.TABLE_EMPTY_CHANGE_SAME_ERROR);
        }
    }

    public boolean isEmpty() {
        return this.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Empty that = (Empty) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
