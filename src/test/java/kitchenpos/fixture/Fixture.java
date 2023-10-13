package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Fixture {
    public Fixture() {
    }

    public static final double MAX_PRICE = 999_999_999_999_999.99;

    public static Menu 메뉴(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroupId, menuProducts);
    }

    public static MenuProduct 메뉴_상품(final Product product, final int quantity) {
        return new MenuProduct(null, null, product.getId(), quantity);
    }

    public static List<MenuProduct> 메뉴_상품_목록(final List<Product> products) {
        List<MenuProduct> menuProducts = products.stream()
                .map(product -> 메뉴_상품(product, 1))
                .collect(Collectors.toList());
        return menuProducts;
    }

    public static Product 상품(final String name, final BigDecimal price) {
        return new Product(null, name, price);
    }

    public static List<Product> 상품_목록(final int amount) {
        final List<Product> products = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            products.add(상품("상품" + i, BigDecimal.ONE));
        }
        return products;
    }

    public static MenuGroup 메뉴_그룹(final String name) {
        return new MenuGroup(null, name);
    }
}
