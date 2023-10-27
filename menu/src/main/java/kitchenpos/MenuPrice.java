package kitchenpos;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MenuPrice {
    private BigDecimal price;

    protected MenuPrice() {
    }

    public MenuPrice(final BigDecimal price) {
        validate(price);
        this.price = price;
    }

    private void validate(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴 가격은 0보다 작을 수 없습니다.");
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MenuPrice menuPrice = (MenuPrice) o;
        return Objects.equals(price, menuPrice.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
