package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;

@Entity
public class Product extends BaseIdEntity {

    private String name;
    private BigDecimal price;

    protected Product() {
    }

    private Product(Long id, String name, BigDecimal price) {
        super(id);
        validate(price);
        this.name = name;
        this.price = price;
    }

    public static Product of(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public static Product entityOf(String name, BigDecimal price) {
        return new Product(null, name, price);
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("Product의 Price는 Null일 수 없습니다.");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product의 Price는 0보다 작을 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + name + '\'' +
            ", price=" + price +
            '}';
    }
}
