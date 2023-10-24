package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    @Column(name = "name")
    private String value;

    public Name() {
    }

    public Name(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException();
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
        final Name name1 = (Name) o;
        return Objects.equals(value, name1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
