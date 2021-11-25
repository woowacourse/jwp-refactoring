package kitchenpos.product.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import kitchenpos.product.exception.InvalidProductNameException;

@Embeddable
public class ProductName {

    @NotNull
    @Column(name = "name")
    private String value;

    protected ProductName() {
    }

    public ProductName(String value) {
        this.value = value;
        validateNull(this.value);
        validateBlank(this.value);
    }

    private void validateNull(String value) {
        if (Objects.isNull(value)) {
            throw new InvalidProductNameException("이름은 Null일 수 없습니다.");
        }
    }

    private void validateBlank(String value) {
        if (value.replaceAll(" ", "").isEmpty()) {
            throw new InvalidProductNameException("이름은 공백으로 이루어질 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductName name = (ProductName) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
