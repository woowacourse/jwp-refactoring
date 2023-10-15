package kitchenpos.domain.product;

import java.util.Objects;

public class ProductName {
    private final String name;

    public ProductName(final String name) {
        validate(name);
        this.name = name;
    }

    private void validate(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductName that = (ProductName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
