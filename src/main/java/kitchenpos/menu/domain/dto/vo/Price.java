package kitchenpos.menu.domain.dto.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;

    @Column(name = "price")
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public static Price of(long value) {
        return new Price(BigDecimal.valueOf(value));
    }

    private void validate(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("금액은 NULL이 될 수 없습니다.");
        }
        if (price.compareTo(MIN_PRICE) < 0) {
            throw new IllegalArgumentException("금액은 음수가 될 수 없습니다.");
        }
    }

    public boolean isBiggerThan(long price) {
        return this.price.longValue() > price;
    }

    public BigDecimal getValue() {
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
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
