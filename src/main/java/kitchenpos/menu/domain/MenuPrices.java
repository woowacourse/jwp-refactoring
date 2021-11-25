package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuPrices {

    private final List<MenuPrice> menuPrices;

    public MenuPrices(List<MenuPrice> menuPrices) {
        this.menuPrices = menuPrices;
    }

    public MenuPrice sumAll() {
        BigDecimal value = menuPrices.stream()
            .map(MenuPrice::getValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MenuPrice(value);
    }
}
