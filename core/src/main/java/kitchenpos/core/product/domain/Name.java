package kitchenpos.core.product.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.springframework.data.relational.core.mapping.Column;

public class Name {
    private static final int MAX_NAME_LENGTH = 255;

    @JsonProperty("name")
    @Column("NAME")
    private final String value;

    public Name(final String value) {
        if (value.isEmpty() || value.length() > MAX_NAME_LENGTH) {
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
        final Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
