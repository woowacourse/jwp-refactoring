package support.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MenuBuilder {

    private static int sequence = 1;

    private final Menu menu;

    public MenuBuilder(final MenuGroup menuGroup) {
        this.menu = new Menu();
        menu.setName("메뉴" + sequence);
        menu.setPrice(BigDecimal.valueOf(0));
        menu.setMenuGroup(menuGroup);
        menu.setMenuProducts(Collections.emptyList());

        sequence++;
    }

    public MenuBuilder setName(final String name) {
        menu.setName(name);
        return this;
    }

    public MenuBuilder setPrice(final BigDecimal price) {
        menu.setPrice(price);
        return this;
    }

    public MenuBuilder setMenuGroupId(final MenuGroup menuGroup) {
        menu.setMenuGroup(menuGroup);
        return this;
    }

    public MenuBuilder setMenuProducts(final Map<Product, Integer> productQuantityMap) {
        final List<MenuProduct> menuProducts = productQuantityMap.entrySet().stream()
                .map(entry -> {
                    final MenuProduct menuProduct = new MenuProduct();
                    menuProduct.setProduct(entry.getKey());
                    menuProduct.setQuantity(entry.getValue());
                    return menuProduct;
                })
                .collect(Collectors.toList());

        menu.setMenuProducts(menuProducts);

        return this;
    }

    public MenuBuilder setMenuProducts(final List<MenuProduct> menuProducts) {
        menu.setMenuProducts(menuProducts);
        return this;
    }

    public Menu build() {
        return menu;
    }
}
