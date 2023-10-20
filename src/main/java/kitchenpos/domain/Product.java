package kitchenpos.domain;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    private static final BigDecimal PRICE_MINIMUM = ZERO;
    private static final int PRICE_PRECISION_MAX = 19;
    private static final int PRICE_SCALE = 2;
    private static final int NAME_LENGTH_MAXIMUM = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    public Product(final Long id, final String name, final BigDecimal price) {
        validateName(name);
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price.setScale(PRICE_SCALE, RoundingMode.DOWN);
    }

    protected Product() {
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    private void validatePrice(final BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("상품 가격은 필수 항목입니다.");
        }
        if (price.compareTo(PRICE_MINIMUM) < 0) {
            throw new IllegalArgumentException("상품 최소 가격은 " + PRICE_MINIMUM + "원입니다.");
        }
        if (price.precision() > PRICE_PRECISION_MAX) {
            throw new IllegalArgumentException("상품 가격은 최대 " + PRICE_PRECISION_MAX + "자리 수까지 가능합니다.");
        }
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

    public BigDecimal getPrice() {
        return price;
    }
}
