package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price")
    private BigDecimal price;

    public Price() {
    }

    private Price(BigDecimal value) {
        this.price = value;
    }

    public static Price of(BigDecimal price) {
        validate(price);
        return new Price(price);
    }

    private static void validate(BigDecimal value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("가격은 null일 수 없습니다.");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
    }

    public BigDecimal add(BigDecimal value) {
        return this.price.add(value);
    }

    public BigDecimal multiply(BigDecimal value) {
        return this.price.multiply(value);
    }

    public BigDecimal getPrice() {
        return price;
    }
}