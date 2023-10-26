package kitchenpos.domain.product;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.vo.Price;

@Entity
public class Product {

    private static final int NAME_LENGTH_MAXIMUM = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(final Long id,
                   final String name,
                   final BigDecimal price) {
        this.id = id;
        validateName(name);
        this.name = name;
        this.price = Price.from(price);
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    private void validateName(final String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("상품 이름은 필수 항목입니다.");
        }
        if (name.length() > NAME_LENGTH_MAXIMUM) {
            throw new IllegalArgumentException("상품 이름의 최대 길이는 " + NAME_LENGTH_MAXIMUM + "입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
