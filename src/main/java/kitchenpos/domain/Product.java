package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import kitchenpos.exception.InvalidProductException;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    protected Product() {
    }

    public Product(String name, BigDecimal price) {
        this(null, name, price);
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
        validateName(this.name);
        validatePrice(this.price);
    }

    private void validateName(String name) {
        validateNull(name);
        validateBlank(name);
    }

    private void validatePrice(BigDecimal price) {
        validateNull(price);
        validateNegative(price);
    }

    private void validateNull(Object object) {
        if (Objects.isNull(object)) {
            throw new InvalidProductException("Product 정보에 null이 포함되었습니다.");
        }
    }

    private void validateBlank(String name) {
        if (name.replaceAll(" ", "").isEmpty()) {
            throw new InvalidProductException("Product name은 공백일 수 없습니다.");
        }
    }

    private void validateNegative(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductException(String.format("Product price는 음수일 수 없습니다. (%s)", price));
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
