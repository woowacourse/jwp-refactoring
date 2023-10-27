package kitchenpos.product.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ProductName {

    private String name;

    protected ProductName() {
    }

    public ProductName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductName that = (ProductName) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
