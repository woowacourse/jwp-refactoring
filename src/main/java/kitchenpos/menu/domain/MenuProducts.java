package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import kitchenpos.menu.exception.MenuPriceExceededException;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts, Long price) {
        validate(menuProducts, price);
        this.menuProducts = menuProducts;
    }

    private void validate(List<MenuProduct> menuProducts, Long price) {
        BigDecimal sum = menuProducts.stream()
            .map(requestDto -> requestDto.getProduct().multiply(requestDto.getQuantity()))
            .reduce(BigDecimal.valueOf(0), BigDecimal::add);

        BigDecimal menuPrice = BigDecimal.valueOf(price);
        if (menuPrice.compareTo(sum) > 0) {
            throw new MenuPriceExceededException();
        }
    }

    public Stream<MenuProduct> stream() {
        return menuProducts.stream();
    }
}
