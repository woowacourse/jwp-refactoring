package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.product.domain.ProductPrice;

@Embeddable
public class MenuPrice {

    @Column(name = "price", nullable = false, scale = 2)
    private BigDecimal value;

    public MenuPrice(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    protected MenuPrice() {
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
    }

    public static MenuPrice from(int value) {
        return new MenuPrice(BigDecimal.valueOf(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuPrice)) {
            return false;
        }
        MenuPrice menuPrice = (MenuPrice) o;
        return Objects.equals(value, menuPrice.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean isGreaterThan(ProductPrice price) {
        return value.compareTo(price.getValue()) > 0;
    }
}
