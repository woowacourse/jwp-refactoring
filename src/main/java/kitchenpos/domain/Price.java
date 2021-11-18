package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    @Column
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격이 0원 미만일 경우 생성할 수 없습니다.");
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal multiply(long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
