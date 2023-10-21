package kitchenpos.fixture;

import static kitchenpos.fixture.ProductFixture.치킨_8000원;
import static kitchenpos.fixture.ProductFixture.피자_8000원;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuFixture {

    public static Menu 치킨_피자_세트_치킨_8000_1개_피자_8000_1개(final MenuGroup menuGroup) {
        return new Menu("치킨 피자 세트", BigDecimal.valueOf(14000), menuGroup, List.of(
                new MenuProduct(치킨_8000원(), 1), new MenuProduct(피자_8000원(), 1)
        ));
    }

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
