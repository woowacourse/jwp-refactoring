package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;

@Entity
public class Product extends BaseEntity {

    private String name;

    private BigDecimal price;

    public Product() {}

    public Product(Long id, String name, BigDecimal price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void validatePrice() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
    }

    public BigDecimal calculatePrice(Long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
