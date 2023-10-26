package kitchenpos.value;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidNumberException;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public Price(final BigDecimal price) {
        validate(price);
        this.price = price;
    }

    protected Price() {
    }

    private void validate(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidNumberException("가격은 음수가 될 수 없습니다.");
        }
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public BigDecimal multiply(final Long quantity) {
        return this.price.multiply(BigDecimal.valueOf(quantity));
    }

    public void isValidPrice(final BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new InvalidNumberException("상품 가격의 총합보다 메뉴가 더 비쌀 수 없습니다.");
        }
    }
}
