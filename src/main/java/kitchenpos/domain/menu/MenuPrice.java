package kitchenpos.domain.menu;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MenuPrice {
    @Column(name = "menu_price")
    private BigDecimal price;

    public MenuPrice() {
    }

    private MenuPrice(BigDecimal price, BigDecimal productsPriceSum) {
        validate(price, productsPriceSum);
        this.price = price;
    }

    public static MenuPrice of(BigDecimal price, BigDecimal productsPriceSum) {
        return new MenuPrice(price, productsPriceSum);
    }

    private void validate(BigDecimal price, BigDecimal productsPriceSum) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        if (price.compareTo(productsPriceSum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
