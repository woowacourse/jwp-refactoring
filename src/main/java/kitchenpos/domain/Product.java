package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Product {

    private static final int MAX_NAME_LENGTH = 255;
    private static final int MIN_PRICE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Column(precision = 19, scale = 2)
    @NotNull
    private BigDecimal price;

    protected Product() {
    }

    private Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static Product create(String name, BigDecimal price) {
        validateName(name);
        validatePrice(price);

        return new Product(name, price);
    }

    private static void validateName(String name) {
        if (name == null) {
            throw new NullPointerException("상품 이름은 null일 수 없습니다.");
        }
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("상품 이름은 1글자 이상, 255자 이하여야 합니다.");
        }
    }

    private static void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new NullPointerException("상품 금액은 null일 수 없습니다.");
        }
        if (price.doubleValue() < MIN_PRICE) {
            throw new IllegalArgumentException("상품 금액은 0원 이상이어야 합니다.");
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
