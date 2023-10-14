package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.common.BaseEntity;

@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    protected Product() {
    }

    public Product(String name, BigDecimal price) {
        this(null, name, price);
    }

    public Product(Long id, String name, BigDecimal price) {
        validate(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("상품의 가격은 0원 이상이어야 합니다.");
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
}
