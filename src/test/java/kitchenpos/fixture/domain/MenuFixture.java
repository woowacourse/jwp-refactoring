package kitchenpos.fixture.domain;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;

public class MenuFixture {

    public static Menu createMenu() {
        final List<MenuProduct> menuProducts = Collections.singletonList(
                new MenuProduct(new Product("후라이드", BigDecimal.valueOf(10_000L)), 2));
        return Menu.builder()
                .id(1L)
                .name("후라이드+후라이드")
                .price(BigDecimal.valueOf(19_000L))
                .menuGroupId(1L)
                .menuProducts(new MenuProducts(menuProducts))
                .build();
    }
}
