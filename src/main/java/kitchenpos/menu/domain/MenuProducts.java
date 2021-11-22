package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuProducts {

    List<MenuProduct> values;

    public MenuProducts(List<MenuProduct> values) {
        this.values = values;
    }

    public void validate(BigDecimal price) {
        if (price.compareTo(sum()) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal sum() {
        return values.stream()
                .map(MenuProduct::amount)
                .reduce(BigDecimal::add)
                .orElseThrow(IllegalArgumentException::new);
    }
}
