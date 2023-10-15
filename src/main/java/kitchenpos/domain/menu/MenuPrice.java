package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.Objects;

public class MenuPrice {
    private final BigDecimal price;
    
    public MenuPrice(final BigDecimal price) {
        this.price = price;
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
