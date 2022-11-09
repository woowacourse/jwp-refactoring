package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuProducts {

    private final List<MenuProduct> value;

    public MenuProducts(final List<MenuProduct> value, final BigDecimal menuPrice) {
        validateExceedPrice(value, menuPrice);
        this.value = value;
    }

    private void validateExceedPrice(final List<MenuProduct> value, final BigDecimal price) {
        final BigDecimal sum = value.stream()
                .map(MenuProduct::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuProduct> getValue() {
        return value;
    }
}
