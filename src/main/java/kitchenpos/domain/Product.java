package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    public Product() {
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    public Product(final Long id, final String name, final BigDecimal price) {
        validate(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validate(final BigDecimal price) {
        if (isPriceNullOrNegative(price)) {
            throw new IllegalArgumentException("상품의 가격은 null 이거나 음수일 수 없습니다.");
        }
    }

    private boolean isPriceNullOrNegative(final BigDecimal price) {
        return price == null || price.compareTo(BigDecimal.ZERO) < 0;
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
