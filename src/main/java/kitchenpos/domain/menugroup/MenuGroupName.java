package kitchenpos.domain.menugroup;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MenuGroupName {
    private String value;

    protected MenuGroupName() {
    }

    public MenuGroupName(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
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
        final MenuGroupName that = (MenuGroupName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
