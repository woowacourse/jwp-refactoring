package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.vo.Money;
import kitchenpos.vo.Quantity;

@Entity
public class Product {

    private static final int MINIMUM_VALUE = 0;

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Money price;

    public Product(Long id, String name, Money price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, Money price) {
        this(null, name, price);
    }

    protected Product() {
    }

    public static Product of(String name, BigDecimal price) {
        validatePrice(price);
        return new Product(name, Money.valueOf(price));
    }

    private static void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < MINIMUM_VALUE) {
            throw new IllegalArgumentException("상품 가격은 " + MINIMUM_VALUE + " 미만일 수 없습니다.");
        }
    }

    public Money calculateTotalPrice(Quantity quantity) {
        return price.multiply(quantity.getValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPriceValue() {
        return price.getValue();
    }

    public Money getPrice() {
        return price;
    }
}
