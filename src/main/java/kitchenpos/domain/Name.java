package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.DomainLogicException;

@Embeddable
public class Name {

    @Column(name = "name", nullable = false)
    private String value;

    protected Name() {
    }

    public Name(final String value) {
        validateNotBlank(value);
        this.value = value;
    }

    private void validateNotBlank(final String value) {
        if (value.isBlank()) {
            throw new DomainLogicException(CustomErrorCode.NAME_BLANK_ERROR);
        }
    }

    public String getValue() {
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
        Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
