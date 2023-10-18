package kitchenpos.domain.menu;

import kitchenpos.exception.MenuPriceException;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MenuPrice {

    private BigDecimal price;

    protected MenuPrice() {
    }

    public MenuPrice(BigDecimal price) {
        validateMenuPrice(price);
        this.price = price;
    }

    private void validateMenuPrice(BigDecimal price) {
        if (isNull(price) || isNegative(price)) {
            throw new MenuPriceException("메뉴 가격은 0 이상이어야 합니다.");
        }
    }

    private boolean isNull(BigDecimal price) {
        return Objects.isNull(price);
    }

    private boolean isNegative(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isGreaterThan(BigDecimal price) {
        if (this.price.compareTo(price) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuPrice menuPrice = (MenuPrice) o;
        return Objects.equals(getPrice(), menuPrice.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrice());
    }
}
