package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;

public class MenuFixture {

    public static Menu 세트_메뉴_1개씩(
            final String name,
            final BigDecimal price,
            final MenuGroup menuGroup,
            final List<Product> products
    ) {
        final List<MenuProduct> menuProducts = products.stream()
                .map(product -> new MenuProduct(product, 1))
                .collect(Collectors.toList());
        return new Menu(name, price, menuGroup, menuProducts);
    }
}
